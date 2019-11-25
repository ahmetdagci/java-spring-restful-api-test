package uk.co.huntersix.spring.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import uk.co.huntersix.spring.rest.exceptionhandler.DuplicateResourceException;
import uk.co.huntersix.spring.rest.exceptionhandler.ResourceNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public Person person(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) throws ResourceNotFoundException {
        return personDataService.findPerson(lastName, firstName);
    }
    
    @GetMapping("/persons/{lastName}/{firstName}")
    public List<Person> persons(@PathVariable(value="lastName") String lastName,
                         @PathVariable(value="firstName") String firstName) {
        return personDataService.findPersons(lastName, firstName);
    }
    
    @GetMapping("/addPerson/{lastName}/{firstName}")
	public ResponseEntity<Person> addPerson(@PathVariable(value="lastName") String lastName,
											@PathVariable(value="firstName") String firstName) throws DuplicateResourceException {	
    	Person person = personDataService.addPerson(lastName, firstName);
    	return new ResponseEntity<Person>(person,HttpStatus.OK);
	}
}