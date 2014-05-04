package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.Address;
import com.example.model.Person;
import com.example.service.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping("/")
    public String listPeople(Map<String, Object> map) {
   		map.put("person", new Person());
        map.put("peopleList", personService.listPeople());

        return "people";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addPerson(@ModelAttribute("person") Person person, BindingResult result) {
    	Address address = new Address();
    	address.setZip("11111");
    	address.setPerson(person);
    	List<Address> addressList = new ArrayList<Address>();
    	addressList.add(address);
    	person.setAddresses(addressList);
    	
        personService.addPerson(person);

        return "redirect:/people/";
    }

    @RequestMapping("/delete/{personId}")
    public String deletePerson(@PathVariable("personId") Integer personId) {

        personService.removePerson(personId);

        return "redirect:/people/";
    }
    
    @RequestMapping("/persons/")
    public String showAll(Map<String, Object> map){
    	System.out.println( "in showall");
    	map.put("peopleList", personService.listPeople());
    	return "redirect:/people/";    	    	
    }
    
    @RequestMapping("/edit/{personId}")
    public String editPerson(@PathVariable Integer personId, Map<String, Object> map ){
    	System.out.println("Got id " + personId );
    	Person person = personService.findPerson(personId);
    	System.out.println( "Name is : " + person.getFirstName() + " " + person.getLastName());
    	System.out.println( "addresses size is " + person.getAddresses().size());
    	map.put("person", person);
    	return "editPerson";
    }
    
    @RequestMapping("/edit/savePerson/")
    public String savePerson(@ModelAttribute("person") Person person, final RedirectAttributes redirectAttributes){
    	System.out.println( "in savePerson - id is " + person.getPersonId()) ;
    	System.out.println( "in savePerson - list size is " + person.getAddresses().size());
    	personService.savePerson(person);
    	redirectAttributes.addFlashAttribute("message", "changes have been saved");
    	
    	return "redirect:/people/";
    }
}
