package com.bugwarsBackend.bugwars.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.bugwarsBackend.bugwars.dto.request.BugParserRequest;
import com.bugwarsBackend.bugwars.service.BugParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BugParserControllerTest {

    @Mock
    private BugParserService bugParserService;

    @InjectMocks
    private BugParserController bugParserController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testParse() {
        BugParserRequest bugParserRequest = new BugParserRequest();
        List<Integer> expectedResult = new ArrayList<>();

        when(bugParserService.parse(bugParserRequest)).thenReturn(expectedResult);

        List<Integer> result = bugParserController.parse(bugParserRequest);

        assertEquals(expectedResult, result);
    }
}