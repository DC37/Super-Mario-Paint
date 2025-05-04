package backend.songs;

/**
 * This gives some note names to values that are used in
 * MIDI. The note name is given first, then either <code>s</code>
 * to mean "sharp" or <code>b</code> to mean flat. Then the constructor
 * is called with an integer value.
 * @author RehdBlob
 * @since 2012.08.22
 */
public enum Note {

    /** Lowest note on the piano. */
    A0(21),
    As0(22), Bb0(22),
    B0(23), Cb1(23),

    /**
     * First octave C on the piano.
     */
    C1(24), Bs0(24),
    Cs1(25), Db1(25),
    D1(26),
    Ds1(27), Eb1(27),
    E1(28), Fb1(28),
    Es1(29), F1(29),
    Fs1(30), Gb1(30),
    G1(31),
    Gs1(32), Ab1(32),
    A1(33),
    As1(34), Bb1(34),
    B1(35), Cb2(35),

    /**
     * Second octave C on the piano.
     */
    C2(36), Bs1(36),
    Cs2(37), Db2(37),
    D2(38),
    Ds2(39), Eb2(39),
    E2(40), Fb2(40),
    Es2(41), F2(41),
    Fs2(42), Gb2(42),
    G2(43),
    Gs2(44), Ab2(44),
    A2(45),
    As2(46), Bb2(46),
    B2(47), Cb3(47),

    /**
     * Third octave C on the piano.
     */
    C3(48), Bs2(48),
    Cs3(49), Db3(49),
    D3(50),
    Ds3(51), Eb3(51),
    E3(52), Fb3(52),
    Es3(53), F3(53),
    Fs3(54), Gb3(54),
    G3(55),
    Gs3(56), Ab3(56),
    A3(57),
    As3(58), Bb3(58),
    B3(59), Cb4(59),

    /**
     * Middle C on the piano.
     */
    C4(60), Bs3(60),
    Cs4(61), Db4(61),
    D4(62),
    Ds4(63), Eb4(63),
    E4(64), Fb4(64),
    Es4(65), F4(65),
    Fs4(66), Gb4(66),
    G4(67),
    Gs4(68), Ab4(68),
    A4(69),
    As4(70), Bb4(70),
    B4(71), Cb5(71),

    /**
     * Fifth octave C on the piano.
     */
    C5(72), Bs4(72),
    Cs5(73), Db5(73),
    D5(74),
    Ds5(75), Eb5(75),
    E5(76), Fb5(76),
    Es5(77), F5(77),
    Fs5(78), Gb5(78),
    G5(79),
    Gs5(80), Ab5(80),
    A5(81),
    As5(82), Bb5(82),
    B5(83), Cb6(83),

    /**
     * Sixth octave C on the piano.
     */
    C6(84), Bs5(84),
    Cs6(85), Db6(85),
    D6(86),
    Ds6(87), Eb6(87),
    E6(88), Fb6(88),
    Es6(89), F6(89),
    Fs6(90), Gb6(90),
    G6(91),
    Gs6(92), Ab6(92),
    A6(93),
    As6(94), Bb6(94),
    B6(95), Cb7(95),

    /**
     * Seventh octave C on the piano.
     */
    C7(96), Bs6(96),
    Cs7(97), Db7(97),
    D7(98),
    Ds7(99), Eb7(99),
    E7(100), Fb7(100),
    Es7(101), F7(101),
    Fs7(102), Gb7(102),
    G7(103),
    Gs7(104), Ab7(104),
    A7(105),
    As7(106), Bb7(106),
    B7(107), Cb8(107),

    /**
     * Highest key on the piano.
     */
    C8(108), Bs7(108);

    /**
     * The int representation of a piano key.
     */
    private int value;

    /**
     * @param num The int representation of a piano
     * key, as initialized.
     */
    private Note(int num) {
        value = num;
    }

    /**
     * @return The MIDI-specified integer that the
     * piano note is assigned to.
     */
    public int getKeyNum() {
        return value;
    }

}
