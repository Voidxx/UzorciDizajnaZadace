package visitori;

import voznja.SegmentVoznje;

public class SegmentVoznjeVisitorImpl implements SegmentVoznjeVisitor {
	   @Override
	   public void visit(SegmentVoznje segmentVoznje) {


		   if(segmentVoznje.getPaketZaDostaviti() != null) {
	       System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s %-20s%n", 
	           segmentVoznje.getVrijemePocetka(), 
	           segmentVoznje.getVrijemeKraja(), 
	           segmentVoznje.getUdaljenostKM(), 
	           segmentVoznje.getTrajanjeVoznje(), 
	           segmentVoznje.getTrajanjeIsporuke(), 
	           segmentVoznje.getUkupnoTrajanjeSegmenta(), 
	           segmentVoznje.getPaketZaDostaviti().getOznaka());
		   }
	   }
}
