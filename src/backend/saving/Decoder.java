package backend.saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

import backend.saving.ams.AMSArrangementDecoder;
import backend.saving.ams.AMSDecoder;
import backend.saving.mpc.MPCArrangementDecoder;
import backend.saving.mpc.MPCDecoder;
import backend.saving.smp.SMPArrangementDecoder;
import backend.saving.smp.SMPDecoder;
import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;

public interface Decoder<T> {
	
	public T decode(File in) throws ParseException, IOException, FileNotFoundException;
	
	/**
	 * Try several parsers in sequence until one succeeds.
	 * @param <T> the generic return type common to all parsers
	 * @param parsers the parsers to try in sequence
	 * @return Either the value returned by the first parser that succeeds, or empty
	 */
	@SafeVarargs
	public static <T> CheckedDecoder<Optional<T>> trySequence(Decoder<? extends T> ... parsers) {
		return new CheckedDecoder<>() {
			
			@Override public Optional<T> decode(File in) throws IOException, FileNotFoundException {
				for (Decoder<? extends T> p : parsers) {
					try {
						T result = p.decode(in);
						return Optional.of(result);
						
					} catch (ParseException e) {
						continue;
					}
				}
				
				return Optional.empty();
			}
			
		};
	}

	public final static Decoder<StaffSequence> MPC_SEQUENCE_DECODER = new MPCDecoder();
	public final static Decoder<StaffSequence> AMS_SEQUENCE_DECODER = new AMSDecoder();
	public final static Decoder<StaffSequence> SMP_SEQUENCE_DECODER = new SMPDecoder();
	
	public final static Decoder<StaffArrangement> MPC_ARRANGEMENT_DECODER = new MPCArrangementDecoder();
	public final static Decoder<StaffArrangement> AMS_ARRANGEMENT_DECODER = new AMSArrangementDecoder();
	public final static Decoder<StaffArrangement> SMP_ARRANGEMENT_DECODER = new SMPArrangementDecoder();
	
	// TODO Add AMS parsers when they are implemented
	// On wrong inputs, the SMP decoder tends to return empty songs instead of throwing exceptions
	// ... This is why we try it last
	public final static CheckedDecoder<Optional<StaffSequence>> SEQUENCE_DECODER = Decoder.trySequence(MPC_SEQUENCE_DECODER, SMP_SEQUENCE_DECODER);
	public final static CheckedDecoder<Optional<StaffArrangement>> ARRANGEMENT_DECODER = Decoder.trySequence(MPC_ARRANGEMENT_DECODER, SMP_ARRANGEMENT_DECODER);
	
	/**
	 * Parsers whose implementation does not throw {@link ParseException}.
	 * @param <T>
	 */
	public static interface CheckedDecoder<T> extends Decoder<T> {
		
		@Override
		public T decode(File in) throws IOException, FileNotFoundException;
		
	}

}
