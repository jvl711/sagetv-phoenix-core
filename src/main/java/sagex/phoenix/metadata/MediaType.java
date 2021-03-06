/**
 *
 */
package sagex.phoenix.metadata;

public enum MediaType {
    TV("TV", "TV"), MOVIE("Movies", "Movie"), MUSIC("Music", "Music"), MUSIC_VIDEOS("MusicVideos", "MusicVideo"),

    // Genre and Actor are special types
    GENRE("Genres", "Genre"), ACTOR("Actors", "Actor"), MOVIE_COLLECTION("MovieCollection", "MovieCollection");

    private String dir = null;
    private String sageValue;

    private MediaType(String dir, String sageValue) {
        this.dir = dir;
        this.sageValue = sageValue;
    }

    public String dirName() {
        return dir;
    }

    public String sageValue() {
        return sageValue;
    }

    public static MediaType toMediaType(String id) {
        if (id == null)
            return null;

        id = id.toLowerCase();
        if ("movie".equals(id) || "movies".equals(id)) {
            return MOVIE;
        }

        if ("tv".equals(id)) {
            return TV;
        }

        if ("genre".equals(id) || "genres".equals(id)) {
            return GENRE;
        }

        if ("actor".equals(id) || "actors".equals(id)) {
            return ACTOR;
        }

        if ("music".equals(id)) {
            return MUSIC;
        }

        if ("musicvideos".equals(id)) {
            return MUSIC;
        }

        return null;
    }

    public static MediaType toMediaType(String id, MediaType def) {
        MediaType mt = toMediaType(id);
        if (mt != null)
            return mt;
        return def;
    }
}