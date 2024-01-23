package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ScriptService {
    @Autowired
   ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;

    public Script getScript(long id, Principal principal) {
        User user = getUser(principal);

        Optional<Script> scriptOptional = scriptRepository.findById(id);

        if(scriptOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Script does not exist");
        } else if (!user.getId().equals(scriptOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must be the owner of this script to access it");
        } else {
            return scriptOptional.get();
        }

    }

    public List<Script> getUserScripts(Principal principal) {
        User user = getUser(principal);

        return scriptRepository.findScriptByUser(user);
    }


    private User getUser(Principal principal) {
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User does not exist.");
        }
    }


}
