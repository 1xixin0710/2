package com.dlut.campusmarket.controller;

import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.ItemQuery;
import com.dlut.campusmarket.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ItemService itemService;

    public DashboardController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Item> items = itemService.search(new ItemQuery());
        int totalCount = items.size();
        model.addAttribute("latestItems", items.stream().limit(8).toList());
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("onSaleCount", countStatus(items, "在售"));
        model.addAttribute("reservedCount", countStatus(items, "已预订"));
        model.addAttribute("completedCount", countStatus(items, "已完成"));

        Map<String, Long> categoryCounts = items.stream()
                .collect(Collectors.groupingBy(
                        Item::getCategory,
                        LinkedHashMap::new,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
        Map<String, Integer> categoryPercents = new LinkedHashMap<>();
        categoryCounts.forEach((category, count) ->
                categoryPercents.put(category, totalCount == 0 ? 0 : (int) Math.round(count * 100.0 / totalCount)));
        model.addAttribute("categoryCounts", categoryCounts);
        model.addAttribute("categoryPercents", categoryPercents);
        return "dashboard";
    }

    private long countStatus(List<Item> items, String status) {
        return items.stream().filter(item -> status.equals(item.getStatus())).count();
    }
}
