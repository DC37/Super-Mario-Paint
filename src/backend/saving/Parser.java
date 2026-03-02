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
import backend.saving.smp.SMPArrangementParser;
import backend.saving.smp.SMPParser;
import backend.songs.StaffArrangement;
import backend.songs.StaffSequence;

public interface Parser<T> {
	
	public T parse(File in) throws ParseException, IOException, FileNotFoundException;
	
	/**
	 * Try several parsers in sequence until one succeeds.
	 * @param <T> the generic return type common to all parsers
	 * @param parsers the parsers to try in sequence
	 * @return Either the value returned by the first parser that succeeds, or empty
	 */
	@SafeVarargs
	public static <T> CheckedParser<Optional<T>> trySequence(Parser<? extends T> ... parsers) {
		return new CheckedParser<>() {
			
			@Override public Optional<T> parse(File in) throws IOException, FileNotFoundException {
				for (Parser<? extends T> p : parsers) {
					try {
						T result = p.parse(in);
						return Optional.of(result);
						
					} catch (ParseException e) {
						continue;
					}
				}
				
				return Optional.empty();
			}
			
		};
	}

	public final static Parser<StaffSequence> MPC_SEQUENCE_PARSER = new MPCDecoder();
	public final static Parser<StaffSequence> AMS_SEQUENCE_PARSER = new AMSDecoder();
	public final static Parser<StaffSequence> SMP_SEQUENCE_PARSER = new SMPParser();
	
	public final static Parser<StaffArrangement> MPC_ARRANGEMENT_PARSER = new MPCArrangementDecoder();
	public final static Parser<StaffArrangement> AMS_ARRANGEMENT_PARSER = new AMSArrangementDecoder();
	public final static Parser<StaffArrangement> SMP_ARRANGEMENT_PARSER = new SMPArrangementParser();
	
	// TODO Add AMS parsers when they are implemented
	public final static CheckedParser<Optional<StaffSequence>> SEQUENCE_PARSER = Parser.trySequence(SMP_SEQUENCE_PARSER, MPC_SEQUENCE_PARSER);
	public final static CheckedParser<Optional<StaffArrangement>> ARRANGEMENT_PARSER = Parser.trySequence(SMP_ARRANGEMENT_PARSER, MPC_ARRANGEMENT_PARSER);
	
	/**
	 * Parsers whose implementation does not throw {@link ParseException}.
	 * @param <T>
	 */
	public static interface CheckedParser<T> extends Parser<T> {
		
		@Override
		public T parse(File in) throws IOException, FileNotFoundException;
		
	}

}
