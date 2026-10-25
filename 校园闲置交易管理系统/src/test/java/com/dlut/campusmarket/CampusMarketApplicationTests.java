package com.dlut.campusmarket;

import com.dlut.campusmarket.mapper.ItemMapper;
import com.dlut.campusmarket.mapper.OrderMapper;
import com.dlut.campusmarket.model.Item;
import com.dlut.campusmarket.model.ItemQuery;
import com.dlut.campusmarket.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CampusMarketApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void protectedPageRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void registeredUserCanLoginAndCreateItem() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "student_test")
                        .param("displayName", "测试同学")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(post("/login")
                        .param("username", "student_test")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        int before = itemMapper.countAll();
        mockMvc.perform(post("/items")
                        .sessionAttr("authUser", testUser())
                        .param("title", "测试台灯")
                        .param("description", "用于验证发布流程的测试物品描述")
                        .param("category", "宿舍生活")
                        .param("price", "66.00")
                        .param("itemCondition", "九成新")
                        .param("tradeLocation", "北门广场")
                        .param("deliveryChoices", "自提")
                        .param("deliveryChoices", "校内送货")
                        .param("publishTime", "2026-10-30T19:30")
                        .param("sellerName", "测试同学")
                        .param("contact", "微信：test_user")
                        .param("status", "在售"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items"));

        assertThat(itemMapper.countAll()).isEqualTo(before + 1);
    }

    @Test
    void invalidItemShowsFormErrors() throws Exception {
        mockMvc.perform(post("/items")
                        .sessionAttr("authUser", testUser())
                        .param("title", "")
                        .param("price", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("items/form"));
    }

    @Test
    void duplicateRegistrationShowsFieldError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "student_test")
                        .param("displayName", "测试同学")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/register")
                        .param("username", "student_test")
                        .param("displayName", "另一位同学")
                        .param("password", "654321"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    void buyerCanPlacePayAndCompleteOrder() throws Exception {
        Item item = itemMapper.search(new ItemQuery()).stream()
                .filter(candidate -> "在售".equals(candidate.getStatus()))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(post("/orders/checkout/" + item.getId())
                        .sessionAttr("authUser", testUser())
                        .param("deliveryMethod", item.getDeliveryOptions().split(",")[0])
                        .param("tradeLocation", item.getTradeLocation())
                        .param("paymentMethod", "校内当面付款")
                        .param("contact", "微信：buyer_test")
                        .param("note", "下课后交易"))
                .andExpect(status().is3xxRedirection());

        Order order = orderMapper.findByBuyer("student_test").get(0);
        assertThat(itemMapper.findById(item.getId()).getStatus()).isEqualTo("已预订");

        mockMvc.perform(post("/orders/" + order.getId() + "/pay")
                        .sessionAttr("authUser", testUser()))
                .andExpect(status().is3xxRedirection());
        assertThat(orderMapper.findByIdAndBuyer(order.getId(), "student_test").getStatus()).isEqualTo("已付款");

        mockMvc.perform(post("/orders/" + order.getId() + "/complete")
                        .sessionAttr("authUser", testUser()))
                .andExpect(status().is3xxRedirection());
        assertThat(orderMapper.findByIdAndBuyer(order.getId(), "student_test").getStatus()).isEqualTo("已完成");
        assertThat(itemMapper.findById(item.getId()).getStatus()).isEqualTo("已完成");
    }

    private com.dlut.campusmarket.model.User testUser() {
        com.dlut.campusmarket.model.User user = new com.dlut.campusmarket.model.User();
        user.setUsername("student_test");
        user.setDisplayName("测试同学");
        return user;
    }
}
