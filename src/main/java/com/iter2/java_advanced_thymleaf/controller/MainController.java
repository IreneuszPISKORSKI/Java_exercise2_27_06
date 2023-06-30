package com.iter2.java_advanced_thymleaf.controller;

import com.iter2.java_advanced_thymleaf.form.CharacterForm;
import com.iter2.java_advanced_thymleaf.character.Character;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("message", message);
        return "index";
    }

    @RequestMapping(value = {"/listCharacter"}, method = RequestMethod.GET)
    public String characterList(Model model){
        RestTemplate restTemplate = new RestTemplate();
        Character[] characters = restTemplate.getForObject("http://localhost:8081/api/character", Character[].class);
        model.addAttribute("characterList", characters);
        return "listCharacter";
    }

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.GET)
    public String editCharacterById(Model model, @PathVariable int id){
        RestTemplate restTemplate = new RestTemplate();
        Character selectedCharacter = restTemplate.getForObject("http://localhost:8081/api/character/" + id, Character.class);
        if (selectedCharacter != null){
            model.addAttribute("characterForm", selectedCharacter);
        }else {
            return "redirect:/listCharacter";
        }
        return "characterDetails";
    }

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.POST)
    public String saveEditCharacter(Model model,//
                                @ModelAttribute("characterForm") CharacterForm characterForm,
                                @PathVariable int id){
        String name = characterForm.getName();
        String type = characterForm.getType();
        int hp = characterForm.getHp();
        RestTemplate restTemplate = new RestTemplate();

        if ( id > 0 && hp > 0 //
                && name != null && name.length() > 0//
                && type != null && type.length() > 0) {
            Character selectedCharacter = restTemplate.getForObject("http://localhost:8081/api/character/" + id, Character.class);
            if (selectedCharacter != null){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                selectedCharacter.setName(name);
                selectedCharacter.setType(type);
                selectedCharacter.setHp(hp);

                HttpEntity<Character> request = new HttpEntity<>(selectedCharacter, headers);
                restTemplate.exchange("http://localhost:8081/api/character/" + id, HttpMethod.PUT, request , Character.class);
            }
            return "redirect:/listCharacter";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";    }

    @RequestMapping(value = {"/addCharacter"}, method = RequestMethod.GET)
    public String showAddCharacterPage(Model model){
        CharacterForm characterForm = new CharacterForm();
        model.addAttribute("characterForm", characterForm);
        return "addCharacter";
    }

    @RequestMapping(value = {"/addCharacter"}, method = RequestMethod.POST)
    public String saveCharacter(Model model,//
            @ModelAttribute("characterForm") CharacterForm characterForm){


        String name = characterForm.getName();
        String type = characterForm.getType();
        int hp = characterForm.getHp();

        if ( hp > 0 //
                && name != null && name.length() > 0//
                && type != null && type.length() > 0) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            RestTemplate restTemplate = new RestTemplate();

            Character newCharacter = new Character(name, type, hp);

            HttpEntity<Character> request = new HttpEntity<>(newCharacter, headers);
            restTemplate.postForEntity("http://localhost:8081/api/character", request , Character.class);
            return "redirect:/listCharacter";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";
    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public String deleteCharacter(@PathVariable int id){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8081/api/character/" + id);
        return "redirect:/listCharacter";
    }
}
