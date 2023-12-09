package visitori;

import objekti.Vozilo;
import voznja.SegmentVoznje;
import voznja.Voznja;

public interface Visitor {
	   void visitVozilo(Vozilo vozilo);
	   void visitVoznja(Voznja voznja);
	   void visitSegmentVoznje(SegmentVoznje segmentVoznje);
}
