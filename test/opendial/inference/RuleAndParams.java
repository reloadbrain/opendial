package opendial.inference;

import static org.junit.Assert.*;
import opendial.DialogueSystem;
import opendial.arch.DialException;
import opendial.bn.distribs.discrete.CategoricalTable;
import opendial.datastructs.Assignment;
import opendial.domains.Domain;
import opendial.readers.XMLDomainReader;
import opendial.state.nodes.ProbabilityRuleNode;

import org.junit.Test;

public class RuleAndParams {

	
	public static final String domainFile = "test//domains//rulesandparams.xml";

	
	@Test
	public void RuleAndParamsTest() throws DialException, InterruptedException {
		
		Domain domain = XMLDomainReader.extractDomain(domainFile);
		DialogueSystem system = new DialogueSystem(domain);
		system.getSettings().showGUI = false;
		system.startSystem();
		assertEquals(system.getContent("theta_moves").toContinuous().getFunction().getMean()[0], 0.2, 0.02);
		assertEquals(system.getContent("a_u^p").toDiscrete().getProb(new Assignment("a_u^p", "I want left")), 0.12, 0.02);
		assertEquals(system.getState().getChanceNode("theta_moves").getOutputNodesIds().size(), 1);
		assertTrue(system.getState().hasChanceNode("movements"));
		assertTrue(system.getState().getChanceNode("movements") instanceof ProbabilityRuleNode);
		CategoricalTable t = new CategoricalTable();
		t.addRow(new Assignment("a_u", "I want left"), 0.8);
		t.addRow(new Assignment("a_u", "I want forward"), 0.1);
		system.addContent(t);
		assertEquals(system.getState().getChanceNode("theta_moves").getOutputNodesIds().size(), 0);
		assertFalse(system.getState().hasChanceNode("movements"));
		assertEquals(system.getContent("theta_moves").toContinuous().getFunction().getMean()[0], 2.0/6, 0.05);
		system.addContent(new Assignment("a_m", "turning left"));
		assertEquals(system.getContent("a_u^p").toDiscrete().getProb(new Assignment("a_u^p", "I want left")), 0.22, 0.03);
		assertEquals(system.getState().getChanceNode("theta_moves").getOutputNodesIds().size(), 1);		
	}
}
