package com.dlut.campusmarket.controller;

import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.ItemQuery;
import com.dlut.campusmarket.model.User;
import com.dlut.campusmarket.service.ItemService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemController {

    private static final List<String> CATEGORIES =
            List.of("电子数码", "书籍资料", "运动户外", "宿舍生活", "其他闲置");
    private static final List<String> CONDITIONS =
            List.of("全新", "九成新", "八成新", "有使用痕迹");
    private static final List<String> LOCATIONS =
            List.of("学苑书院", "西山生活区", "北门广场", "伯川图书馆", "刘长春体育馆");
    private static final List<String> STATUSES =
            List.of("在售", "已预订", "已完成");
    private static final List<String> DELIVERY_OPTIONS =
            List.of("自提", "校内送货", "邮寄");

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String list(@ModelAttribute("query") ItemQuery query, Model model) {
        model.addAttribute("items", itemService.search(query));
        addOptions(model);
        return "items/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = requireItem(id);
        item.setDeliveryChoices(Arrays.asList(item.getDeliveryOptions().split(",")));
        model.addAttribute("item", item);
        return "items/detail";
    }

    @GetMapping("/new")
    public String createPage(Model model, HttpSession session) {
        Item item = new Item();
        item.setStatus("在售");
        item.setItemCondition("九成新");
        item.setTradeLocation("学苑书院");
        item.setPublishTime(LocalDateTime.now().withSecond(0).withNano(0));
        User user = (User) session.getAttribute("authUser");
        item.setSellerName(user.getDisplayName());
        model.addAttribute("item", item);
        model.addAttribute("mode", "create");
        addOptions(model);
        return "items/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            addOptions(model);
            return "items/form";
        }
        itemService.create(item);
        redirectAttributes.addFlashAttribute("success", "闲置物品已发布");
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        Item item = requireItem(id);
        item.setDeliveryChoices(item.getDeliveryOptions() == null
                ? List.of()
                : Arrays.asList(item.getDeliveryOptions().split(",")));
        model.addAttribute("item", item);
        model.addAttribute("mode", "edit");
        addOptions(model);
        return "items/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") Item item,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            addOptions(model);
            return "items/form";
        }
        if (!itemService.update(id, item)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "物品不存在");
        }
        redirectAttributes.addFlashAttribute("success", "物品信息已更新");
        return "redirect:/items";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!itemService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "物品不存在");
        }
        redirectAttributes.addFlashAttribute("success", "物品记录已删除");
        return "redirect:/items";
    }

    private Item requireItem(Long id) {
        Item item = itemService.findById(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "物品不存在");
        }
        return item;
    }

    private void addOptions(Model model) {
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("conditions", CONDITIONS);
        model.addAttribute("locations", LOCATIONS);
        model.addAttribute("statuses", STATUSES);
        model.addAttribute("deliveryOptions", DELIVERY_OPTIONS);
    }
}
