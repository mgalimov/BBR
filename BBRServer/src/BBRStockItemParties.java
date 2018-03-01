import javax.servlet.annotation.WebServlet;
import BBRCust.BBRStockItemParty;
import BBRCust.BBRStockItemPartyManager;

@WebServlet("/BBRStockItemParties")
public class BBRStockItemParties extends
		BBRBasicServlet<BBRStockItemParty, BBRStockItemPartyManager> {
	private static final long serialVersionUID = 1L;

	public BBRStockItemParties() throws InstantiationException,
			IllegalAccessException {
		super(BBRStockItemPartyManager.class);
	}
}
