package graphpackage;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;

public class GraphFrame extends JFrame{
	
	private JPanel[] type_panels;
	private GraphGenerate gg = new GraphGenerate();
	
	private HashSet<String> Specs;
	private Iterator<String> it;
	
	GridBagLayout layout;
	GridBagConstraints constraints;
	
	private Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN};
	private Random rand = new Random();
	
	public GraphFrame(){
		Specs = gg.getSpecs();
		int length = Specs.size();
		System.out.println(length);
		int length1 = colors.length;
		type_panels = new JPanel[length];
		//it = Specs.iterator();
		int i=0, j=0;
		
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		this.setLayout(layout);
		
		while(i<length){
			type_panels[i] = new JPanel();
			type_panels[i].setPreferredSize(new Dimension(50, 100));
			type_panels[i].setBackground(colors[rand.nextInt(length1)]);
			type_panels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
			constraints.weightx = 0;
			constraints.weighty = 0;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			this.addComponent(type_panels[i], (i%10), (i/10), 1, 1);
			i++;
		}
		
	}
	
	public void addComponent(Component comp, int x, int y, int width, int height){
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(comp, constraints);
		this.add(comp);
	}	

}
