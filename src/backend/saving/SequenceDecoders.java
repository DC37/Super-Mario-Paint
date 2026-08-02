package backend.saving;

import java.util.Optional;

import backend.saving.ams.AMSDecoder;
import backend.saving.mpc.MPCDecoder;
import backend.saving.smp.SMPDecoder;
import backend.songs.Song;

public enum SequenceDecoders {

	MPC(new MPCDecoder()),
	AMS(new AMSDecoder()),
	SMP(new SMPDecoder());
	
	private Decoder<Song> decoder;
	
	private SequenceDecoders(Decoder<Song> decoder) {
		this.decoder = decoder;
	}
	
	public Decoder<Song> getDecoder() {
		return decoder;
	}
	
	public static CheckedDecoder<Optional<Song>> getAllTryable() {
		// On wrong inputs, the SMP decoder tends to return empty songs
		// instead of throwing exceptions. This is why we try it last.
		return CheckedDecoder.of(MPC.getDecoder(), SMP.getDecoder());
	}
	
}
