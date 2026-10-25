package com.dlut.campusmarket.controller;

import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.Order;
import com.dlut.campusmarket.model.User;
import com.dlut.campusmarket.service.ItemService;
import com.dlut.campusmarket.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final List<String> PAYMENT_METHODS =
            List.of("校内当面付款", "微信转账", "支付宝转账");

    private final OrderService orderService;
    private final ItemService itemService;

    public OrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("orders", orderService.findByBuyer(user.getUsername()));
        return "orders/list";
    }

    @GetMapping("/checkout/{itemId}")
    public String checkout(@PathVariable Long itemId, HttpSession session, Model model,
                           RedirectAttributes redirectAttributes) {
        Item item = requireItem(itemId);
        if (!"在售".equals(item.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "该物品当前不可购买");
            return "redirect:/items/" + itemId;
        }
        Order order = new Order();
        order.setDeliveryMethod(firstDelivery(item));
        order.setTradeLocation(item.getTradeLocation());
        order.setPaymentMethod(PAYMENT_METHODS.get(0));
        model.addAttribute("item", item);
        model.addAttribute("order", order);
        addOptions(item, model);
        return "orders/checkout";
    }

    @PostMapping("/checkout/{itemId}")
    public String placeOrder(@PathVariable Long itemId,
                             @Valid @ModelAttribute("order") Order order,
                             BindingResult bindingResult,
                             HttpSession session, Model model,
                             RedirectAttributes redirectAttributes) {
        Item item = requireItem(itemId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("item", item);
            addOptions(item, model);
            return "orders/checkout";
        }
        try {
            Order created = orderService.placeOrder(item, currentUser(session), order);
            redirectAttributes.addFlashAttribute("success", "订单已创建，请核对卖家信息并完成付款");
            return "redirect:/orders/" + created.getId();
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/items/" + itemId;
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        model.addAttribute("order", requireOrder(id, currentUser(session)));
        return "orders/detail";
    }

    @PostMapping("/{id}/pay")
    public String pay(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            orderService.markPaid(id, currentUser(session).getUsername());
            redirectAttributes.addFlashAttribute("success", "已记录付款，请与卖家完成验货交接");
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancel(id, currentUser(session).getUsername());
            redirectAttributes.addFlashAttribute("success", "订单已取消，物品重新上架");
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            orderService.complete(id, currentUser(session).getUsername());
            redirectAttributes.addFlashAttribute("success", "交易已完成，感谢使用大工校园二手");
        } catch (IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    private Item requireItem(Long id) {
        Item item = itemService.findById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "物品不存在");
        }
        item.setDeliveryChoices(Arrays.asList(item.getDeliveryOptions().split(",")));
        return item;
    }

    private Order requireOrder(Long id, User user) {
        Order order = orderService.findByIdAndBuyer(id, user.getUsername());
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在");
        }
        return order;
    }

    private User currentUser(HttpSession session) {
        return (User) session.getAttribute("authUser");
    }

    private String firstDelivery(Item item) {
        return item.getDeliveryOptions().split(",")[0];
    }

    private void addOptions(Item item, Model model) {
        model.addAttribute("deliveryMethods", Arrays.asList(item.getDeliveryOptions().split(",")));
        model.addAttribute("paymentMethods", PAYMENT_METHODS);
    }
}
