package com.bugwarsBackend.bugwars.controller;

import com.bugwarsBackend.bugwars.dto.request.ScriptRequest;
import com.bugwarsBackend.bugwars.dto.response.ScriptName;
import com.bugwarsBackend.bugwars.model.Script;
import com.bugwarsBackend.bugwars.service.ScriptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScriptControllerTest {

    @Mock
    private ScriptService scriptService;

    @InjectMocks
    private ScriptController scriptController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllValidScripts() {
        List<ScriptName> scriptNames = new ArrayList<>();

        when(scriptService.getAllValidScripts()).thenReturn(scriptNames);

        List<ScriptName> result = scriptController.getAllValidScripts();

        assertEquals(scriptNames, result);
    }

    @Test
    void testGetUserScripts() {
        List<Script> scripts = new ArrayList<>();
        Principal principal = () -> "username";

        when(scriptService.getUserScripts(principal)).thenReturn(scripts);

        List<Script> result = scriptController.getUserScripts(principal);

        assertEquals(scripts, result);
    }

    @Test
    void testGetScriptById() {
        Script script = new Script();
        Long id = 1L;
        Principal principal = () -> "username";

        when(scriptService.getScriptById(id, principal)).thenReturn(script);

        Script result = scriptController.getScriptById(id, principal);

        assertEquals(script, result);
    }

    @Test
    void testCreateScript() {
        ScriptRequest request = new ScriptRequest();
        Script script = new Script();
        Principal principal = () -> "username";

        when(scriptService.createScript(request, principal)).thenReturn(script);

        Script result = scriptController.createScript(request, principal);

        assertEquals(script, result);
    }

    @Test
    void testUpdateScript() {
        ScriptRequest request = new ScriptRequest();
        Script script = new Script();
        Long id = 1L;
        Principal principal = () -> "username";

        when(scriptService.updateScript(id, request, principal)).thenReturn(script);

        Script result = scriptController.updateScript(id, principal, request);

        assertEquals(script, result);
    }

    @Test
    void testDeleteScriptById() {
        Long id = 1L;
        Principal principal = () -> "username";

        scriptController.deleteScriptById(id, principal);

        verify(scriptService, times(1)).deleteScriptById(id, principal);
    }
}
