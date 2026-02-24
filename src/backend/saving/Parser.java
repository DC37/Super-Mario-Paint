package backend.saving;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

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

	public final static Parser<StaffSequence> MPC_SEQUENCE_PARSER = new MPCDecoder();
	public final static Parser<StaffSequence> AMS_SEQUENCE_PARSER = new AMSDecoder();
	public final static Parser<StaffSequence> SMP_SEQUENCE_PARSER = new SMPParser();
	
	public final static Parser<StaffArrangement> MPC_ARRANGEMENT_PARSER = new MPCArrangementDecoder();
	public final static Parser<StaffArrangement> AMS_ARRANGEMENT_PARSER = new AMSArrangementDecoder();
	public final static Parser<StaffArrangement> SMP_ARRANGEMENT_PARSER = new SMPArrangementParser();

}
