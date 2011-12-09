package org.openjpa.ide.idea.config.swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.openjpa.ide.idea.config.AffectedModule;

/**
 */
public class AffectedModulesRowModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final List<AffectedModule> affectedModules;

    public AffectedModulesRowModel(final List<AffectedModule> affectedModules) {
        this.affectedModules = affectedModules;
    }

    public List<AffectedModule> getAffectedModules() {
        return this.affectedModules;
    }

    @Override
    public int getRowCount() {
        return this.affectedModules.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return columnIndex == 0 ? "Enabled" : "Affected Project Module";
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return columnIndex == 0 ? Boolean.class : String.class;
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final AffectedModule am = this.affectedModules.get(rowIndex);
        return columnIndex == 0 ? am.isEnabled() : am.getName();
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            final AffectedModule am = this.affectedModules.get(rowIndex);
            am.setEnabled((Boolean) aValue);
            this.fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

}
