package org.api.subsonic.model.res.playlists;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayLists {
	
	@JsonProperty("playlist")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<PlaylistItem> playlist;
}