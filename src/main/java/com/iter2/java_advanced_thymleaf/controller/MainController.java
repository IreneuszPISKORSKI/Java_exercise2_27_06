package com.iter2.java_advanced_thymleaf.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.iter2.java_advanced_thymleaf.form.CharacterForm;
import com.iter2.java_advanced_thymleaf.character.Character;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class MainController {

    private static final List<Character> characterList = new ArrayList<Character>();
    private int lastId=2;

    static {
        characterList.add(new Character(1,"name1", "Mage", 8));
        characterList.add(new Character(2,"name2", "Warrior", 10));
    }

    // Injectez (inject) via application.properties.
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
        model.addAttribute("characterList", characterList);
        return "listCharacter";
    }

    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.GET)
    public String ditCharacterById(Model model, @PathVariable Integer id){
        Optional<Character> selectedCharacter = characterList.stream().filter(ch->ch.getId()==id).findFirst();
        if (selectedCharacter.isPresent()){
            model.addAttribute("characterForm", selectedCharacter.get());
        }else {
            return "redirect:/listCharacter";
        }
        return "characterDetails";
    }
    @RequestMapping(value = {"/edit/{id}"}, method = RequestMethod.POST)
    public String saveEditCharacter(Model model,//
                                @ModelAttribute("characterForm") CharacterForm characterForm,
                                @PathVariable Integer id){
        String name = characterForm.getName();
        String type = characterForm.getType();
        int hp = characterForm.getHp();

        if ( id > 0 && hp > 0 //
                && name != null && name.length() > 0//
                && type != null && type.length() > 0) {
            Optional<Character> selectedCharacter = characterList.stream().filter(ch->ch.getId()==id).findFirst();
            if (selectedCharacter.isPresent()){
                selectedCharacter.get().setName(characterForm.getName());
                selectedCharacter.get().setType(characterForm.getType());
                selectedCharacter.get().setHp(characterForm.getHp());
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
            lastId++;
            int id = lastId;
            Character newCharacter = new Character(id, name, type, hp);
            characterList.add(newCharacter);

            return "redirect:/listCharacter";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "addCharacter";    }

    @RequestMapping(value = {"/delete/{id}"}, method = RequestMethod.GET)
    public String deleteCharacter(@PathVariable int id){
        Character selectedCharacter = characterList.stream().filter(ch->ch.getId()==id).findFirst().get();

        characterList.remove(selectedCharacter);

        return "redirect:/listCharacter";
    }
}
