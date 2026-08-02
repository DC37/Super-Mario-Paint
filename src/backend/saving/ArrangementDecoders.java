package backend.saving;

import java.util.Optional;

import backend.saving.ams.AMSArrangementDecoder;
import backend.saving.mpc.MPCArrangementDecoder;
import backend.saving.smp.SMPArrangementDecoder;
import backend.songs.Arrangement;

public enum ArrangementDecoders {

	MPC(new MPCArrangementDecoder()),
	AMS(new AMSArrangementDecoder()),
	SMP(new SMPArrangementDecoder());
	
	private Decoder<Arrangement> decoder;
	
	private ArrangementDecoders(Decoder<Arrangement> decoder) {
		this.decoder = decoder;
	}
	
	public Decoder<Arrangement> getDecoder() {
		return decoder;
	}
	
	public static Decoder<Optional<Arrangement>> getAllTryable() {
		// On wrong inputs, the SMP decoder tends to return empty songs
		// instead of throwing exceptions. This is why we try it last.
		return CheckedDecoder.of(MPC.getDecoder(), SMP.getDecoder());
	}
	
}
