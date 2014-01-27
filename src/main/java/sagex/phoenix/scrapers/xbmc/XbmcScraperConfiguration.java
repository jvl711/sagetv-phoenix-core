package sagex.phoenix.scrapers.xbmc;


/**
 * Unique Case, does not use Field and Group proxies
 * 
 * @author seans
 *
 */
public class XbmcScraperConfiguration {
    public XbmcScraperConfiguration() {
    }

    public String getScraperProperty(String scraperId, String key, String defValue) {
        Object o = phoenix.config.GetProperty(String.format("bmt/xbmc/%s/%s",scraperId,key), defValue);
        if (o==null) {
            return defValue;
        } else {
            return String.valueOf(o);
        }
    }

    public void setScraperProperty(String scraperId, String key, String value) {
        phoenix.config.SetProperty(String.format("bmt/xbmc/%s/%s",scraperId,key), value);
    }
}
