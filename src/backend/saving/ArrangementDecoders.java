package backend.saving;

import java.io.IOException;
import java.util.Optional;

import backend.saving.ams.AMSArrangementDecoder;
import backend.saving.mpc.MPCArrangementDecoder;
import backend.saving.smp.SMPArrangementDecoder;
import backend.songs.Arrangement;

public enum ArrangementDecoders {

	MPC(new MPCArrangementDecoder()),
	AMS(new AMSArrangementDecoder()),
	SMP(new SMPArrangementDecoder());
	
	private Decoder<Arrangement, IOException> decoder;
	
	private ArrangementDecoders(Decoder<Arrangement, IOException> decoder) {
		this.decoder = decoder;
	}
	
	public Decoder<Arrangement, IOException> getDecoder() {
		return decoder;
	}
	
	public static CheckedDecoder<Optional<Arrangement>> getAllTryable() {
		// When loading an arrangement, it is assumed to be SMP first.
		// If it doesn't work, it is then processed as an MPC file.
		return CheckedDecoder.of(SMP.getDecoder(), MPC.getDecoder());
	}
	
}
