package sagex.phoenix.vfs.filters;

import sagex.phoenix.factory.ConfigurableOption;
import sagex.phoenix.factory.ConfigurableOption.DataType;
import sagex.phoenix.factory.ConfigurableOption.ListSelection;
import sagex.phoenix.vfs.IMediaResource;
import sagex.phoenix.vfs.util.ConfigList;

import java.util.Map;

public class TitleStartsWithFilter extends Filter {
    private String value = "";
    boolean ignoreThe = false;
    boolean ignoreAll = false;

    public TitleStartsWithFilter() {
        super();
        addOption(new ConfigurableOption(OPT_VALUE, "Starts With", "A", DataType.string, true, ListSelection.single,
                ConfigList.mediaTypeList()));
        addOption(new ConfigurableOption("ignore-the", "Disregard 'the' when sorting", "false", DataType.bool, true,
                ListSelection.single, "true:Yes,no:No"));
        addOption(new ConfigurableOption("ignore-all", "Disregard 'a', 'an', and 'the' when sorting", "false", DataType.bool, true,
                ListSelection.single, "true:Yes,no:No"));
    }

    @Override
    public boolean canAccept(IMediaResource res) {
        if (ignoreAll || ignoreThe) {
            return removeLeadingArticles(res.getTitle(), (ignoreAll)).toLowerCase().startsWith(value.toLowerCase());
        } else {
            return res.getTitle().toLowerCase().startsWith(value.toLowerCase());
        }
    }

    public static String removeLeadingArticles(final String in, boolean all) {
        String out = in;

        if (in.toLowerCase().startsWith("the ")) {
            return out.substring(4);
        }

        if (all) {
            if (in.toLowerCase().startsWith("a ")) {
                return out.substring(2);
            }

            if (in.toLowerCase().startsWith("an ")) {
                return out.substring(3);
            }
        }

        return out;
    }

    @Override
    protected void onUpdate() {
        value = getOption(OPT_VALUE).getString(null);
        ignoreThe = getOption("ignore-the").getBoolean(false);
        ignoreAll = getOption("ignore-all").getBoolean(false);
    }

    @Override
    public Map<String, String> getOptionList(String id) {
        return super.getOptionList(id);
    }

}
