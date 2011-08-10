package au.org.ala.delta.intkey.ui;

import java.util.HashSet;
import java.util.Set;

import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.format.ItemFormatter;

public class TaxonCellRenderer extends ColoringListCellRenderer {

    protected ItemFormatter _formatter;
    protected Set<Item> _taxaToColor;

    public TaxonCellRenderer() {
        _formatter = new ItemFormatter(false, true, false, false, true, false);
        _taxaToColor = new HashSet<Item>();
    }

    @Override
    protected String getTextForValue(Object value) {
        if (value instanceof Item) {
            return _formatter.formatItemDescription((Item) value);
        } else {
            return value.toString();
        }
    }

    @Override
    protected boolean isValueColored(Object value) {
        if (value instanceof Item) {
            return _taxaToColor.contains(value);
        } else {
            return false;
        }
    }

    public void setTaxaToColor(Set<Item> taxaToColor) {
        if (taxaToColor != null) {
            _taxaToColor = new HashSet<Item>(taxaToColor);
        } else {
            _taxaToColor = new HashSet<Item>();
        }
    }

}
