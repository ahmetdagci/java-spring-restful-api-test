package uk.co.huntersix.spring.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import uk.co.huntersix.spring.rest.exceptionhandler.DuplicateResourceException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;
    
    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }
    
    @Test
    public void shouldNotReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Ahmet", "Smith"));
        this.mockMvc.perform(get("/person/ahmet/smith"))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldReturnEmptyPersonListFromService() throws Exception {
        when(personDataService.findPersons(any(), any())).thenReturn(Lists.emptyList());
        this.mockMvc.perform(get("/persons/jack/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
    
    @Test
    public void shouldReturnPersonListFromService() throws Exception {
        when(personDataService.findPersons(any(), any())).thenReturn(Lists.list(new Person("Simith","Marry")));
        this.mockMvc.perform(get("/persons/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void shouldReturnDuplicatePersonFromService() throws Exception {
    	 doThrow(new DuplicateResourceException("Smith,Marry with name already exist")).when(personDataService).addPerson("Smith", "Mary");
    	 this.mockMvc.perform(get("/addPerson/Smith/Mary").contentType(MediaType.APPLICATION_JSON))
    	 	   .andExpect(status().isConflict())
    	       .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    	       .andExpect(jsonPath("$.message").value("Smith,Marry with name already exist"));
    }
    
    @Test
    public void shouldAddPersonFromService() throws Exception {
    	Person person = new Person("Smith", "Mary");
    	given(personDataService.addPerson(any(), any())).willReturn(person);
    	this.mockMvc.perform(get("/addPerson/Smith/Mary").contentType(MediaType.APPLICATION_JSON))
    	 	   .andExpect(status().isOk())
    	       .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    	 	   .andExpect(jsonPath("$.firstName", is(person.getFirstName())));
    }
    
}