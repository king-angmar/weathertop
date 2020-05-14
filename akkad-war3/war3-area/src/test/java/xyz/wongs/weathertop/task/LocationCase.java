package xyz.wongs.weathertop.task;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.wongs.weathertop.base.BaseTest;

public class LocationCase extends BaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetById() throws Exception{
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/locations/getUrl")
                .param("id","143840")
                .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("32.html"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testGetById3() throws Exception{
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/locations/findByLocationId")
                .param("id","143840").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testGetById4() throws Exception{
        MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/locations/findByLocationId")
                .param("id","143840").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status=mvcResult.getResponse().getStatus();
        Assert.assertEquals(200,status);
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
