package au.org.ala.delta.intkey.ui;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;

public class IntkeyDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -2611321681800876175L;
    
    public IntkeyDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    public IntkeyDialog(Dialog owner, boolean modal) {
        super(owner, modal);
        init();
    }
    
    private void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
        
    @Override
    public String getName() {
        return null;
    }
}