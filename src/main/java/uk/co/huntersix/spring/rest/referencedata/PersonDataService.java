package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;

import uk.co.huntersix.spring.rest.exceptionhandler.DuplicateResourceException;
import uk.co.huntersix.spring.rest.exceptionhandler.ResourceNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    public Person findPerson(String lastName, String firstName) throws ResourceNotFoundException {
         Person person = PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
            		&& p.getLastName().equalsIgnoreCase(lastName))
            .findAny()
            .orElse(null);
         if(person==null){
        	 throw new ResourceNotFoundException(lastName.concat(",").concat(firstName).concat(" with name does not exist"));
         }
         return person;
    }
    
    public List<Person> findPersons(String lastName, String firstName){
        return PERSON_DATA.stream()
           .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
           		&& p.getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());
   }
   
    
    public Person addPerson(String lastName, String firstName) throws DuplicateResourceException {
    	boolean personExist = PERSON_DATA.stream().anyMatch(p -> p.getFirstName().equalsIgnoreCase(firstName)
	            		&& p.getLastName().equalsIgnoreCase(lastName));
    	if(personExist){
    		throw new DuplicateResourceException(lastName.concat(",").concat(firstName).concat(" with name already exist"));
    	}
    	Person person = new Person(firstName,lastName);
    	PERSON_DATA.stream().collect(Collectors.toList()).add(person);
    	return person;
    }
    
}
