package sagex.phoenix.metadata.provider.tmdb3;

import org.junit.BeforeClass;
import org.junit.Test;
import sagex.phoenix.Phoenix;
import sagex.phoenix.configuration.proxy.GroupProxy;
import sagex.phoenix.metadata.*;
import sagex.phoenix.metadata.search.HasFindByIMDBID;
import sagex.phoenix.metadata.search.MetadataSearchUtil;
import sagex.phoenix.metadata.search.SearchQuery;
import sagex.phoenix.metadata.search.SearchQuery.Field;
import sagex.phoenix.util.DateUtils;
import test.InitPhoenix;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class TMDB3DetailParserTest {
    @BeforeClass
    public static void init() throws IOException {
        InitPhoenix.init(true, true);
    }

    @Test
    public void testDetailsByIMDBID() throws MetadataException, IOException {
        TMDB3MetadataProvider provider = (TMDB3MetadataProvider) Phoenix.getInstance().getMetadataManager().getProvider("tmdb3");
        assertNotNull("TMDB3 is not registered.", provider);

        IMetadata md = ((HasFindByIMDBID) provider).getMetadataForIMDBId("tt1228705");
        verifyMetadata(md);
    }

    @Test
    public void testDetailsByID() throws MetadataException, IOException {
        TMDB3MetadataProvider provider = (TMDB3MetadataProvider) Phoenix.getInstance().getMetadataManager().getProvider("tmdb3");
        assertNotNull("TMDB3 is not registered.", provider);

        SearchQuery q = new SearchQuery(MediaType.MOVIE, Field.ID, "10138");
        List<IMetadataSearchResult> results = provider.search(q);
        assertEquals(1, results.size());
        verifyMetadata(provider.getMetaData(results.get(0)));
    }

    @Test
    public void testSearch() throws MetadataException {
        TMDB3MetadataProvider provider = (TMDB3MetadataProvider) Phoenix.getInstance().getMetadataManager().getProvider("tmdb3");
        assertNotNull("TMDB3 is not registered.", provider);

        SearchQuery q = new SearchQuery(MediaType.MOVIE, "Iron Man 2", "2010");
        // set the title in the QUERY fields, since providers look in the QUERY
        // field
        q.set(Field.QUERY, "Iron Man 2");
        List<IMetadataSearchResult> results = provider.search(q);
        assertTrue(results.size() > 0);

        for (IMetadataSearchResult r : results) {
            System.out.printf("RESULT: %s; %s; %s\n", r.getTitle(), r.getYear(), r.getScore());
        }

        // Iron Man 2 should be first result
        IMetadataSearchResult sr = results.get(0);
        assertEquals("10138", sr.getId());
        assertEquals("tmdb3", sr.getProviderId());
        assertEquals(MediaType.MOVIE, sr.getMediaType());
        assertEquals("Iron Man 2", sr.getTitle());
        assertEquals("2010", String.valueOf(sr.getYear()));
        assertTrue(sr.getScore() > .9);
    }

    public void verifyMetadata(IMetadata md) {
        assertEquals("Iron Man 2", md.getMediaTitle());
        assertTrue(md.getActors().size() > 10);
        boolean foundCast = false;
        for (ICastMember cm : md.getActors()) {
            if (cm.getName().equals("Robert Downey Jr.")) {
                foundCast = true;
                assertEquals("Robert Downey Jr.", cm.getName());
                assertEquals("Tony Stark / Iron Man", cm.getRole());
            }
        }
        assertTrue("No cast found", foundCast);

        assertNotNull(md.getDescription());
        assertTrue(md.getDescription().length() > 50);

        assertTrue(md.getDirectors().size() > 0);
        assertEquals("Iron Man 2", md.getEpisodeName());

        assertTrue(md.getGenres().size() > 0);
        assertEquals("Adventure", md.getGenres().get(0));

        assertEquals("tt1228705", md.getIMDBID());
        assertEquals("10138", md.getMediaProviderDataID());
        assertEquals("tmdb3", md.getMediaProviderID());
        assertEquals("Iron Man 2", md.getMediaTitle());
        assertEquals(MediaType.MOVIE.sageValue(), md.getMediaType());
        assertEquals(DateUtils.parseDate("2010-05-07").getTime(), md.getOriginalAirDate().getTime());
        assertEquals("PG-13", md.getRated());
        // no extended ratings
        // assertTrue(md.getExtendedRatings().length()>4);
        assertEquals(MetadataSearchUtil.convertTimeToMillissecondsForSage("124"), md.getRunningTime());
        assertEquals("Iron Man 2", md.getEpisodeName());
        assertNull(md.getRelativePathWithTitle());

        assertTrue("Invalid User Rating: " + md.getUserRating(), md.getUserRating() > 0);

        assertTrue(md.getDirectors().size() > 0);
        assertTrue(md.getWriters().size() > 0);
        assertTrue(md.getProducers().size() > 0);
        assertEquals(2010, md.getYear());

        assertTrue(md.getTagLine().length() > 0);

        assertTrue(md.getTrailerUrl().length() > 0);
        assertTrue(md.getTrailerUrl().contains("www.youtube.com"));

        TMDB3Configuration config = GroupProxy.get(TMDB3Configuration.class);
        assertEquals(count(md.getFanart(), MediaArtifactType.POSTER), config.getMaxPosters());
        assertEquals(count(md.getFanart(), MediaArtifactType.BACKGROUND), config.getMaxBackgrounds());

        for (IMediaArt ma : md.getFanart()) {
            assertTrue(ma.getDownloadUrl().startsWith("http"));
        }

        // assertTrue(md.getQuotes().length()>0);
        // assertTrue(md.getTrivia().length()>0);
    }

    private int count(List<IMediaArt> fanart, MediaArtifactType type) {
        int count = 0;
        for (IMediaArt ma : fanart) {
            if (ma.getType() == type)
                count++;
        }
        return count;
    }
}
