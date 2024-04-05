package com.bugwarsBackend.bugwars.service;

import com.bugwarsBackend.bugwars.dto.request.ScriptRequest;
import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.model.User;
import com.bugwarsBackend.bugwars.parser.BugParser;
import com.bugwarsBackend.bugwars.parser.BugParserException;
import com.bugwarsBackend.bugwars.repository.ScriptRepository;
import com.bugwarsBackend.bugwars.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScriptService {

    @Autowired
    ScriptRepository scriptRepository;

    @Autowired
    UserRepository userRepository;



    public List<ScriptName> getAllValidScripts() {
        List<ScriptName> result = scriptRepository.getAllValidScripts();

        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No valid scripts found");
        } else {
            return result;
        }
    }

    public List<Script> getUserScripts(Principal principal) {
        User user = getUser(principal);

        return scriptRepository.getScriptsByUser(user);
    }

    public Script getScriptById(Long id, Principal principal) {
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



    public Script createScript(ScriptRequest request, Principal principal) {
        User user = getUser(principal);
        Script script = new Script();
        BugParser parser = new BugParser();

        if (scriptRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Script name already exists");
        }

        List<Integer> byteCode = null;
        // TODO parse method is not working correctly
        try {
           byteCode = parser.parse(request.getRaw());
            script.setBytecodeValid(true);

        } catch (BugParserException e) {
            script.setBytecodeValid(false);
            script.setBytecode(new int[]{});
        }
        script.setName(request.getName());
        script.setRaw(request.getRaw());
        script.setUser(user);
        script.setBytecode(formatBytecode(byteCode));

        return scriptRepository.save(script);
    }

    public Script updateScript(Long id, ScriptRequest request, Principal principal) {
        //Not done
        User user = getUser(principal);
        Optional<Script> scriptOptional = scriptRepository.findById(id);
        BugParser parser = new BugParser();
        Script updatedScript;

        if(scriptOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Script does not exist");
        } else if (!scriptOptional.get().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must be the owner of this script to access it");
        } else {
            updatedScript = scriptOptional.get();
        }

        List<Integer> byteCode = null;

        try {
            byteCode = parser.parse(request.getRaw());
            updatedScript.setBytecodeValid(true);

        } catch (BugParserException e) {
            updatedScript.setBytecodeValid(false);
            updatedScript.setBytecode(new int[]{});
        }
        updatedScript.setName(request.getName());
        updatedScript.setRaw(request.getRaw());
        updatedScript.setUser(user);
        updatedScript.setBytecode(formatBytecode(byteCode));

        return scriptRepository.save(updatedScript);
    }

    public void deleteScriptById(Long id, Principal principal) {
        User user = getUser(principal);
        Optional<Script> scriptOptional = scriptRepository.findById(id);

        if (scriptOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Script does not exist");
        }

        if (!user.getId().equals(scriptOptional.get().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to delete this script. You must be the owner of this script to delete it");
        }

        scriptRepository.deleteById(id);
    }


    private User getUser(Principal principal) {
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist.");
        }
    }


    public static int[] formatBytecode(@NotBlank @Size(max = 10000) List<Integer> byteCode) {
        if (byteCode == null) {
            throw new IllegalArgumentException("Bytecode is null");
        }
        return byteCode.stream().mapToInt(Integer::intValue).toArray();
    }
}