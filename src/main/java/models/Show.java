package models;


public class Show {
    private int id;
    private String title;
    private String summary;
    private String release_date;
    private int networkId;
    private int seasons;

    public Show(String title, String summary, String release_date, int networkId, int seasons) {
        this.title = title;
        this.summary = summary;
        this.release_date = release_date;
        this.networkId = networkId;
        this.seasons = seasons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Show show = (Show) o;

        if (id != show.id) return false;
        if (networkId != show.networkId) return false;
        if (seasons != show.seasons) return false;
        if (!title.equals(show.title)) return false;
        if (summary != null ? !summary.equals(show.summary) : show.summary != null) return false;
        return release_date != null ? release_date.equals(show.release_date) : show.release_date == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (release_date != null ? release_date.hashCode() : 0);
        result = 31 * result + networkId;
        result = 31 * result + seasons;
        return result;
    }
}
