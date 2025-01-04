package com.music;

import com.music.controller.HomeController;
import com.music.repository.MusicTrackRepository;
import com.music.services.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private MusicTrackRepository musicTrackRepository;

	@InjectMocks
	private HomeController musicTrackController;

	private MockHttpSession session;

	@BeforeEach
	void setup() throws Exception {
		musicTrackRepository.deleteAll();
		MvcResult loginResult = mockMvc.perform(post("/login")
						.param("username", "user")
						.param("password", "12345")
				)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andReturn();

		session = (MockHttpSession) loginResult.getRequest().getSession();
	}

	@Test
	void downloadTrackNotFoundUnitTest() {
		Long trackId = 1L;
		when(musicTrackRepository.findById(trackId)).thenReturn(Optional.empty());

		ResponseEntity<byte[]> response = musicTrackController.downloadTrack(trackId);

		assertEquals(404,response.getStatusCodeValue());
	}

	@Test
	void downloadTrackNotFoundIntegrationTest() throws Exception {
		mockMvc.perform(get("/download/999").session(session))
				.andExpect(status().isNotFound());
	}
}
