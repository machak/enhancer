package org.openjpa.ide.idea.config;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.intellij.ui.BooleanTableCellEditor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import org.apache.commons.lang.Validate;
import org.openjpa.ide.idea.EnhancerSupportRegistry;
import org.openjpa.ide.idea.PersistenceApi;
import org.openjpa.ide.idea.config.swing.AffectedModulesRowModel;
import org.openjpa.ide.idea.config.swing.ColumnAdjuster;
import org.openjpa.ide.idea.config.swing.MetadataOrClassFilesRowModel;
import org.openjpa.ide.idea.integration.EnhancerSupport;

/**
 */
public class ConfigForm {


    private GuiState guiState = null;

    private JPanel configPanel;

    private JCheckBox enableEnhancerCheckBox;


    private JTable affectedModulesTable;

    private JTable metadataAndClassesTable;

    private JPanel contentPanel;

    private JPanel infoPanel;

    private JCheckBox includeTestClassesCheckBox;

    private JScrollPane metaDataAndClassesScrollPane;

    private JPanel indexNotReadyPanel;

    private JRadioButton hibernateRadioButton;

    private JRadioButton jPARadioButton;

    private JComboBox persistenceImplComboBox;
    private JCheckBox addDefaultConstructor;
    private JCheckBox enforcePropertyRestrictions;

    //
    // Interface with ProjectComponent
    //

    public JComponent getRootComponent() {
        return this.configPanel;
    }

    //
    // Gui methods
    //

    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall"})
    public boolean isModified() {
        final GuiState data = this.guiState;
        if (this.enableEnhancerCheckBox.isSelected() != data.isEnhancerEnabled()) {
            return true;
        }


        if (this.includeTestClassesCheckBox.isSelected() != data.isIncludeTestClasses()) {
            return true;
        }
        if (this.addDefaultConstructor.isSelected() != data.isAddDefaultConstructor()) {
            return true;
        }
        if (this.enforcePropertyRestrictions.isSelected() != data.isEnforcePropertyRestrictions()) {
            return true;
        }
        if (!this.hibernateRadioButton.isSelected() && PersistenceApi.HIBERNATE == data.getApi()) {
            return true;
        }
        if (!this.jPARadioButton.isSelected() && PersistenceApi.JPA == data.getApi()) {
            return true;
        }

        final EnhancerSupport enhancerSupport = getByEnhancerSupportName(data, (String) this.persistenceImplComboBox.getSelectedItem());
        if (!data.getEnhancerSupport().getId().equals(enhancerSupport.getId())) {
            return true;
        }

        final PersistenceApi selectedApi = this.hibernateRadioButton.isSelected() ? PersistenceApi.HIBERNATE : PersistenceApi.JPA;
        if (data.getApi() != selectedApi) {
            return true;
        }

        final AffectedModulesRowModel affectedModulesRowModel = (AffectedModulesRowModel) this.affectedModulesTable.getModel();
        final List<AffectedModule> affectedModules = affectedModulesRowModel.getAffectedModules();
        boolean modified = affectedModules != null ? !affectedModules.equals(data.getAffectedModules()) : data.getAffectedModules() != null;
        if (modified) {
            return modified;
        }
        final MetadataOrClassFilesRowModel metadataOrClassFilesRowModel = (MetadataOrClassFilesRowModel) this.metadataAndClassesTable.getModel();
        final List<MetaDataOrClassFile> files = metadataOrClassFilesRowModel.getFiles();
        return files != null ? !files.equals(data.getMetadataFiles()) : data.getMetadataFiles() != null;

    }

    @SuppressWarnings("FeatureEnvy")
    private void createUIComponents() {

        //
        // ComboBox for selecting persistence implementation

        this.persistenceImplComboBox = new JComboBox();
        this.persistenceImplComboBox.addActionListener(new ActionListener() {
            @SuppressWarnings("MagicCharacter")
            @Override
            public void actionPerformed(final ActionEvent e) {

                if ("comboBoxChanged".equals(e.getActionCommand())) {
                    final EnhancerSupport selectedEnhancerSupport =
                            getByEnhancerSupportName(ConfigForm.this.guiState,
                                    (String) ConfigForm.this.persistenceImplComboBox.getSelectedItem());

                    final PersistenceApi selectedApi =
                            ConfigForm.this.hibernateRadioButton.isSelected() ? PersistenceApi.HIBERNATE : PersistenceApi.JPA;
                    final PersistenceApi supportedSelectedApi =
                            selectedEnhancerSupport.isSupported(selectedApi) ? selectedApi
                                    : selectedEnhancerSupport.getDefaultPersistenceApi();

                    if (selectedApi != supportedSelectedApi) {
                        JOptionPane.showMessageDialog(null, "Selected persistence implementation does not support "
                                + selectedApi
                                + ','
                                + "\nreverting to " + supportedSelectedApi);
                    }

                    ConfigForm.this.hibernateRadioButton.setSelected(PersistenceApi.HIBERNATE == supportedSelectedApi);
                    ConfigForm.this.jPARadioButton.setSelected(PersistenceApi.JPA == supportedSelectedApi);
                    ConfigForm.this.hibernateRadioButton.setEnabled(selectedEnhancerSupport.isSupported(PersistenceApi.HIBERNATE));
                    ConfigForm.this.jPARadioButton.setEnabled(selectedEnhancerSupport.isSupported(PersistenceApi.JPA));
                    ConfigForm.this.persistenceImplComboBox.setSelectedItem(selectedEnhancerSupport.getName());

                    ConfigForm.this.configPanel.repaint();
                }
            }
        });


    }

    //
    // Utility methods
    //

    static public void setPreferredTableHeight(final JTable table, final int rows) {
        final int width = table.getPreferredSize().width;
        final int height = rows * table.getRowHeight();
        table.setPreferredSize(new Dimension(width, height));
    }

    private static EnhancerSupport getByEnhancerSupportName(final GuiState guiState, final String selectedItem) {
        EnhancerSupport ret = null;
        final EnhancerSupportRegistry enhancerSupportRegistry = guiState.getEnhancerSupportRegistry();
        for (final EnhancerSupport enhancerSupport : enhancerSupportRegistry.getSupportedEnhancers()) {
            final String enhancerSupportName = enhancerSupport.getName();
            if (enhancerSupportName.equals(selectedItem)) {
                ret = enhancerSupport;
                break;
            }
        }
        Validate.notNull(ret, "EnhancerSupport value is not supported! value=" + selectedItem);
        return ret;
    }


    //
    // IDEA UI-Designer code (automatically generated, so do not touch!)
    //

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        configPanel = new JPanel();
        configPanel.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        enableEnhancerCheckBox = new JCheckBox();
        enableEnhancerCheckBox.setText("Enable Enhancer");
        configPanel.add(enableEnhancerCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK
                        | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED,
                null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(panel1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null,
                null, null, 0, false));
        hibernateRadioButton = new JRadioButton();
        hibernateRadioButton.setText("HIBERNATE");
        panel1.add(hibernateRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        jPARadioButton = new JRadioButton();
        jPARadioButton.setText("JPA");
        panel1.add(jPARadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                false));
        configPanel.add(persistenceImplComboBox,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        final JLabel label1 = new JLabel();
        label1.setText(" Metadata file extensions (use ';' to separate)");
        configPanel.add(label1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                false));
        includeTestClassesCheckBox = new JCheckBox();
        includeTestClassesCheckBox.setText("Include Test classes");
        configPanel.add(includeTestClassesCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK
                        | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        indexNotReadyPanel = new JPanel();
        indexNotReadyPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(indexNotReadyPanel, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Please wait until indexing is finished");
        indexNotReadyPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null,
                null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        configPanel.add(contentPanel, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Affected Modules"));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
                null, 0, false));
        affectedModulesTable = new JTable();
        affectedModulesTable.setEnabled(true);
        affectedModulesTable.setFillsViewportHeight(false);
        affectedModulesTable.setPreferredScrollableViewportSize(new Dimension(450, 30));
        scrollPane1.setViewportView(affectedModulesTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder("Metadata and annotated classes for enhancement"));
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(infoPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
                null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Please click 'Make Project' to see affected files");
        infoPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                false));
        metaDataAndClassesScrollPane = new JScrollPane();
        panel3.add(metaDataAndClassesScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK
                        | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK
                        | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        metadataAndClassesTable = new JTable();
        metadataAndClassesTable.setFillsViewportHeight(false);
        metadataAndClassesTable.setFont(new Font(metadataAndClassesTable.getFont().getName(), metadataAndClassesTable.getFont().getStyle(),
                metadataAndClassesTable.getFont().getSize()));
        metadataAndClassesTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
        metaDataAndClassesScrollPane.setViewportView(metadataAndClassesTable);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(hibernateRadioButton);
        buttonGroup.add(jPARadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return configPanel;
    }

    //
    // Gui methods
    //

    @SuppressWarnings({"MagicNumber", "FeatureEnvy", "ChainedMethodCall"})
    public void setData(final GuiState data) {
        this.guiState = new GuiState(data);

        //
        // Basic panels
        this.indexNotReadyPanel.setVisible(!data.isIndexReady());
        this.contentPanel.setVisible(data.isIndexReady());

        //
        // Enable enhancer checkbox
        this.enableEnhancerCheckBox.setSelected(data.isEnhancerEnabled());

        //
        // Persistence implementation selection
        final EnhancerSupportRegistry enhancerSupportRegistry = data.getEnhancerSupportRegistry();
        final Set<EnhancerSupport> supportedEnhancers = enhancerSupportRegistry.getSupportedEnhancers();
        if (this.persistenceImplComboBox.getItemCount() == 0) {
            for (final EnhancerSupport support : supportedEnhancers) {
                this.persistenceImplComboBox.addItem(support.getName());
            }
        }
        final EnhancerSupport enhancerSupport = data.getEnhancerSupport();
        this.persistenceImplComboBox.setSelectedItem(enhancerSupport.getName());
        if (supportedEnhancers.size() <= 1) {
            this.persistenceImplComboBox.setVisible(false);
        } else {
            this.persistenceImplComboBox.setVisible(true);
        }

        // just to be sure -> validate persistence settings from config file
        PersistenceApi persistenceApi = data.getApi();
        if (!enhancerSupport.isSupported(persistenceApi)) {
            persistenceApi = enhancerSupport.getDefaultPersistenceApi();
        }

        this.hibernateRadioButton.setSelected(PersistenceApi.HIBERNATE == persistenceApi);
        this.jPARadioButton.setSelected(PersistenceApi.JPA == persistenceApi);
        this.hibernateRadioButton.setEnabled(enhancerSupport.isSupported(PersistenceApi.HIBERNATE));
        this.jPARadioButton.setEnabled(enhancerSupport.isSupported(PersistenceApi.JPA));


        //
        // Test classes inclusion
        this.includeTestClassesCheckBox.setSelected(data.isIncludeTestClasses());
        this.addDefaultConstructor.setSelected(data.isAddDefaultConstructor());
        this.enforcePropertyRestrictions.setSelected(data.isEnforcePropertyRestrictions());

        //
        // Panel displaying an info message if enhancer is not initialized

        this.infoPanel.setVisible(!data.isEnhancerInitialized());
        this.infoPanel.setEnabled(!data.isEnhancerInitialized());

        //
        // Table displaying affected modules if enhancer is initialized

        final TableModel affectedModulesRowModel = new AffectedModulesRowModel(data.getAffectedModules());
        // modules affected by class enhancement
        this.affectedModulesTable.setModel(affectedModulesRowModel);
        // set column appearance
        final TableColumnModel columnModel = this.affectedModulesTable.getColumnModel();
        final TableColumn firstColumn = columnModel.getColumn(0);
        firstColumn.setMinWidth(50);
        firstColumn.setMaxWidth(50);
        firstColumn.setPreferredWidth(150);
        this.affectedModulesTable.setDefaultEditor(Boolean.class, new BooleanTableCellEditor(false));
        setPreferredTableHeight(this.affectedModulesTable, this.affectedModulesTable.getRowCount());

        //
        // Table displaying affected files/classes/.. if enhancer is initialized

        final TableModel metadataOrClassFilesRowModel = new MetadataOrClassFilesRowModel(data.getMetadataFiles(), data.getAnnotatedClassFiles());
        // files affected by class enhancement
        this.metadataAndClassesTable.setModel(metadataOrClassFilesRowModel);
        // set column appearance
        this.metadataAndClassesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.metadataAndClassesTable.setDefaultEditor(Boolean.class, new BooleanTableCellEditor(false));
        // adjust column sizes (after being rendered the first time - necessary for ColumnAdjuster to work)
        final ColumnAdjuster columnAdjuster = new ColumnAdjuster(this.metadataAndClassesTable);
        //columnAdjuster.setOnlyAdjustLarger(false);
        columnAdjuster.setDynamicAdjustment(true);
        columnAdjuster.adjustColumns();
        setPreferredTableHeight(this.metadataAndClassesTable, this.metadataAndClassesTable.getRowCount());

        this.metadataAndClassesTable.setVisible(data.isEnhancerInitialized());

        // only display detected classes if initialized
        this.metaDataAndClassesScrollPane.setVisible(data.isEnhancerInitialized());
    }

    @SuppressWarnings("FeatureEnvy")
    public void getData(final GuiState data) {
        data.setEnhancerEnabled(this.enableEnhancerCheckBox.isSelected());

        data.setIncludeTestClasses(this.includeTestClassesCheckBox.isSelected());
        data.setAddDefaultConstructor(this.addDefaultConstructor.isSelected());
        data.setEnforcePropertyRestrictions(this.enforcePropertyRestrictions.isSelected());

        final EnhancerSupport enhancerSupport = getByEnhancerSupportName(data, (String) this.persistenceImplComboBox.getSelectedItem());
        data.setEnhancerSupport(enhancerSupport);

        final PersistenceApi selectedApi = this.hibernateRadioButton.isSelected() ? PersistenceApi.HIBERNATE : PersistenceApi.JPA;

        final boolean apiSupported = enhancerSupport.isSupported(selectedApi);
        final PersistenceApi supportedApi = apiSupported ? selectedApi : enhancerSupport.getDefaultPersistenceApi();
        data.setApi(supportedApi);
        data.setAffectedModules(((AffectedModulesRowModel) this.affectedModulesTable.getModel()).getAffectedModules());
        data.setMetadataFiles(((MetadataOrClassFilesRowModel) this.metadataAndClassesTable.getModel()).getFiles());

    }


    public boolean isModified(GuiState data) {
        if (enableEnhancerCheckBox.isSelected() != data.isEnhancerEnabled()) {
            return true;
        }
        if (includeTestClassesCheckBox.isSelected() != data.isIncludeTestClasses()) {
            return true;
        }
        return false;
    }
}
