package com.jagsits.service.sudoku;

import com.jagsits.BaseSpringTest;
import com.jagsits.service.sudoku.solver.SudokuSolverAlgorithm;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@WebAppConfiguration
@EnableAutoConfiguration
public class SudokuServiceTest extends BaseSpringTest {

    @Autowired
    private DefaultSudokuService service;

    private static final Map<String, String> solutionMap = new HashMap<>();

    @BeforeClass
    public static void setup() {
        solutionMap.put(SudokuBoardTest.BOARD_STRING_UNSOLVED, SudokuBoardTest.BOARD_STRING_SOLVED);
        solutionMap.put("7.....4...2..7..8...3..8..9...5..3...6..2..9...1..7..6...3..9...3..4..6...9..1..5", "798635421126974583453218679972586314564123897381497256617352948835749162249861735");
        solutionMap.put("4...3.......6..8..........1....5..9..8....6...7.2........1.27..5.3....4.9........", "468931527751624839392578461134756298289413675675289314846192753513867942927345186");
        solutionMap.put("7.8...3.....2.1...5.........4.....263...8.......1...9..9.6....4....7.5...........", "728946315934251678516738249147593826369482157852167493293615784481379562675824931");
        solutionMap.put("3.7.4...........918........4.....7.....16.......25..........38..9....5...2.6.....", "317849265245736891869512473456398712732164958981257634174925386693481527528673149");
        solutionMap.put("7.8...3.....6.1...5.........4.....263...8.......1...9..9.2....4....7.5...........", "768942315934651278512738649147593826329486157856127493693215784481379562275864931");
        solutionMap.put("5..7..6....38...........2..62.4............917............35.8.4.....1......9....", "582743619963821547174956238621479853348562791795318426217635984439287165856194372");
        solutionMap.put("4..7..6....38...........2..62.5............917............43.8.5.....1......9....", "482731659963852417175964238621579843358426791794318526217643985539287164846195372");
        solutionMap.put(".4..1.2.......9.7..1..........43.6..8......5....2.....7.5..8......6..3..9........", "347516289258349176619872543591437628823961754476285931735198462182654397964723815");
        solutionMap.put("7.5.....2...4.1...3.........1.6..4..2...5...........9....37.....8....6...9.....8.", "745896132928431756361725849519683427274159368836247591652378914483912675197564283");
        solutionMap.put(".8..1......5....3.......4.....6.5.7.89....2.....3.....2.....1.9..67........4.....", "382514697145976832769238415421695378893147256657382941274853169936721584518469723");
        solutionMap.put("......41.9..3.....3...5.....48..7..........62.1.......6..2....5.7....8......9....", "857629413924318657361754928248167539793485162516932784689271345172543896435896271");
        solutionMap.put("7.5.....2...4.1...3.........1.6..4..2...5...........9....37.....9....8...8.....6.", "745896312928431756361527984519683427274159638836742591652378149193264875487915263");
        solutionMap.put("8.9...3.....7.1...5.........7.....263...9.......1...4..6.2....4....8.5...........", "819462375632751489547839612178543926324697158956128743763215894291384567485976231");
        solutionMap.put("1...48....5....9....6...3.....57.2..8.3.........9............4167..........2.....", "139648752758132964246795318461573289893426175527981436982357641674819523315264897");
        solutionMap.put("6.9.....8...7.1...4............6...4.2.....3..3....5...1.5...7.8...9..........2..", "679325148283741965451986327598263714124857639736419582912534876867192453345678291");
        solutionMap.put("8.5.....2...9.1...3.........6.7..4..2...5...........6....38.....1....9...4.....7.", "895476312426931857371528694569713428284659731137842569952387146713264985648195273");
        solutionMap.put("......41.9..3.....3...2.....48..7..........52.1.......5..2....6.7....8......9....", "827569413964318527351724968248157639793486152615932784589271346172643895436895271");
        solutionMap.put("4.3.....2...6.1...8...........5..97.2...3.....1..........84.....9....6...7.....5.", "463957812927681345851324769386512974249736581715498236632845197594173628178269453");
        solutionMap.put("..1.....7...89..........6..26..3.......5...749...........1.4.5.83.............2..", "681243597573896412429715638264937185318562974957481326796124853832659741145378269");
        solutionMap.put("3.7..4.2....1..8..9............3..9..5.8......4.6...........5.12...7..........6..", "317584926526193847984762315672435198153829764849617253798246531265371489431958672");
        solutionMap.put("......41.9..3.....3...5.....48..7..........52.1.......6..2....5.7....8......9....", "857962413924318567361754298548127936793486152216539784689271345175643829432895671");
        solutionMap.put("4.3.....2...6.1...8...........5..79.2...3.....1..........84.....9....6...7.....5.", "463957812927681345851324967386512794249736581715498236632845179594173628178269453");
        solutionMap.put("....2..4..7...6....1.5.....2......8....3..7..4.9.........6..1.38...9..........5..", "598723641374816952612549837237961485186354729459278316745682193863195274921437568");
        solutionMap.put("7.8...3.....6.1...4.........6.....253...8.......1...9..9.5....2....7.4...........", "718925364935641278426738519167493825359286147842157693693514782281379456574862931");
        solutionMap.put("8.5.....2...9.1...3.........6.7..4..2...5...........6....38.....4....7...1.....9.", "895476132426931857371825946569713428284659371137248569952387614643192785718564293");
        solutionMap.put("8.5.....2...9.1...3.........6.7..4..2...5...........6....38.....1....7...4.....9.", "895476132426931857371528946569713428284659371137842569952387614613294785748165293");
        solutionMap.put("2...4.5...1.....3............6...8.2.7.3.9......1.....4...5.6.....7...9...8......", "239847561614592738785613924396475812871329456542168379427951683163784295958236147");
        solutionMap.put(".......71.2.8........5.3...7.9.6.......2..8..1.........3...25..6...1..4..........", "854926371326871954917543682789165423543297816162438795431782569675319248298654137");
        solutionMap.put("7.4.....2...8.1...3.........5.6..1..2...4...........9....37.....8....6...9.....5.", "714956832925831746368724519459683127271549368836217495642375981583192674197468253");
        solutionMap.put("....4...1.3.6.....8........1.9..5.........87....2......7....26.5...94.........3..", "297543681435681927816927543129875436653419872748236195374158269562394718981762354");
        solutionMap.put("8.5.....2...4.1...3.........6.7..4..2...5...........9....38.....1....7...9.....6.", "845967132926431857371528649569713428284659371137842596752386914613294785498175263");
        solutionMap.put(".1.62....5......43....9....7......8...5.....4...1..........36...9....2..8....7...", "319624857562871943478395162721439586985762314643158729254983671197546238836217495");
        solutionMap.put("7.4.....2...8.1...3.........5.6..1..2...4...........9....37.....9....5...8.....6.", "714956382925831746368427951459683127271549638836712495642375819193268574587194263");
        solutionMap.put("...3.9.7.8..4.....1........2..5..6...3.....4.....1....5.....8......2.1.....7....9", "465389271827451396193267458278594613631872945954613782549136827786925134312748569");
        solutionMap.put("..36......4.....8.9.....7..86.4...........1.5.2.......5...17...1...9...........2.", "253678419647931582918524736861459273439782165725163894592317648184296357376845921");
        solutionMap.put(".......91.7..3....82..........1.5...3.....7.....9.......16...5...4.2....7.....8..", "463758291179236548825491376247165983396842715518973624931687452684529137752314869");
        solutionMap.put(".8.....63....4.2............1.8.35..7.....9.....6.....2.9.7...........354........", "584729163396148257127536849612893574753214986948657321239475618871962435465381792");
        solutionMap.put("8.5.....2...9.1...3.........6.7..4..2...5...........6....38.....4....6...9.....7.", "815674392426931857379528146561793428284156739937842561652387914743219685198465273");
        solutionMap.put(".5.4.9......6....12.....3..7.3...2.....5...9.1.........68....4.....8........7....", "851439627937652481246718359793841265684527193125963874368195742572384916419276538");
        solutionMap.put("3..8.1....5....6.9......4..5..7...8..4..6...........2.2..3.........9.1....7......", "369841275854273619172659438521734986748962351936185724295316847483597162617428593");
        solutionMap.put(".4.7...6...39............57.......3.2...8.....19...57.6...4.....5.1......2...6.84", "842715963573962841196834257768591432235487196419623578687249315354178629921356784");
        solutionMap.put("7.4.....2...8.1...3.........5.6..1..2...4...........5....37.....9....6...8.....9.", "714596832925831746368427915459683127271945368836712459642379581193258674587164293");
        solutionMap.put("5..6.3....2....98.......1...1..9.......3....67.......4....8.25.4..7..............", "598613472126475983347829165614597328852341796739268514963184257481752639275936841");
        solutionMap.put("2.8.5.......7...4.3........5...2.9.......1......6......7.1.4.6.......3.2.1.......", "298456137156793248347812659564328971839571426721649583972134865485967312613285794");
        solutionMap.put("...9.31..5.7....8.2.........4....6......5..2..1.......8...7.......6..4.....3....9", "486923157597461283231587946945732618678159324312846795869274531153698472724315869");
        solutionMap.put("......41.6..3.....3...2.....49..8..........52.1.......5..6....7.8....9......3....", "928576413674319528351824679249158736836497152715263894592681347183742965467935281");
        solutionMap.put("7.....48....6.1..........2....3..6.52...8..............53.....1.6.1.........4.7..", "712935486384621597596874123849312675235786914671459238953267841467198352128543769");
        solutionMap.put("5.8.....7...9.1...4............5...4.6.....3..9....6...2.3...1.7...8..........2..", "518263497672941853439875162387156924164729538295438671826394715751682349943517286");
        solutionMap.put("2...6...8.743.........2....62......1...4..5..8...........5..34......1..........7.", "259164738174398625386725914623957481791482563845613297918576342467231859532849176");
        solutionMap.put("6.9.....8...3.1...4............6...4.2.....3..7....5...1.5...7.8...9..........2..", "639752148287341965451986327598263714124875639376419582913524876862197453745638291");
        solutionMap.put(".6..5.4.3.2.1...........7..4.3...6..7..5........2.........8..5.6...4...........1.", "167852493924173568538496721453719682712568349896234175241387956685941237379625814");
        solutionMap.put("5.7....3.....61...1.8......62..4.......7...8...........1....6.43..5...........2..", "567492138239861745148375926623148579951736482874259361712983654396524817485617293");
        solutionMap.put("4.3.....2...6.1...8...........5..97.2...3.....7..........84.....9....6...1.....5.", "463957812927681345851324769386512974249736581175498236632845197594173628718269453");
        solutionMap.put("8.5.....2...4.1...3.........6.7..4..2...5...........6....38.....9....7...1.....9.", "845697132926431857371825946569713428284956371137248569752389614493162785618574293");
        solutionMap.put(".....1..8.9....3..2........5......84.7.63.......9.....1.4....5.....7.6.....2.....", "647321598895467312213589476536712984978634125421958763164893257382175649759246831");
        solutionMap.put("......41.9..2.....3...5.....48..7..........62.1.......6..5....3.7....8......9....", "827639415954218637361754928248167359793485162516923784689571243175342896432896571");
        solutionMap.put(".6..2...1...3...7..1.......3.49.....7.....2........5.8....586.........4.9........", "863427951549361872217895463354982716786513294192674538431758629678239145925146387");
        solutionMap.put("...9.31..6.7....8.2.........5....4......6..2..1.......8...7.......3..5.....4....9", "584923167697541283231687954356792418978164325412835796845279631769318542123456879");
        solutionMap.put("6..1...8..53.............4....8...6..9....7....24.........7.3.9....2.5..1........", "679143285453268197281759643715832964894516732362497851528674319946321578137985426");
        solutionMap.put("4.3.....2...7.1...9...........5..81.2...3.....8..........94.....7....6...6.....5.", "413865972628791345957324168396572814241638597785419236532946781874153629169287453");
        solutionMap.put("4.3.....2...7.1...9...........5..18.2...3.....8..........94.....7....6...6.....5.", "413865972628791345957324861396572184241638597785419236532946718874153629169287453");
        solutionMap.put("1..46...5.2....7......9.....3.7.8..........91...2........3..84.6........5........", "178462935924531768365897412239718654857643291416259387791325846683174529542986173");
        solutionMap.put("4.35...2.....61...7............895.....3..8..2...........4...7..9....6...1.......", "463598721928761453751234968376189542145327896289645317832456179597812634614973285");
        solutionMap.put(".6..2...1...3...7..1.......3.49.....7.....2........5.8....856.........4.9........", "863427951549361872217598463384952716756813294192674538431785629678239145925146387");
        solutionMap.put("3.7..4.2....1..5..9............3..9..5.8......4.6...........8.12...7..........6..", "317564928428193567965782314872435196659817243143629785794256831286371459531948672");
        solutionMap.put("4.1.6....3.....2........8..15.2.....6......1....9......2.7.8..........43.7.......", "491862375387195264265374891159236487632487519748951632923748156816529743574613928");
        solutionMap.put("...8...3...5...7.....1.........5.9..18.......3..4.......7..2..6....7.5...4.....1.", "276845139415936728938127465764253981182769354359481672597312846821674593643598217");
        solutionMap.put("7.....48....6.1..........2....3..6.52...8..............63.....1.5.1.........4.7..", "712935486384621597596874123849312675235786914671459238463297851957168342128543769");
        solutionMap.put("48.3............71.2.......7.5....6....2..8.............1.76...3.....4......5....", "487312695593684271126597384735849162914265837268731549851476923379128456642953718");
        solutionMap.put("4.3.....2...6.1...8...........5..79.2...3.....7..........84.....9....6...1.....5.", "463957812927681345851324967386512794249736581175498236632845179594173628718269453");
        solutionMap.put(".5..7..83..4....6.....5....83.6........9..1...........5.7...4.....3.2...1........", "951476283284193567376258914835621749642937158719584326527869431498312675163745892");
        solutionMap.put("....3..715..4.2............2..6..4...38.7..............7..8..1.6..5..2...........", "824935671567412893193768542251693487938274156746851329472386915619547238385129764");
        solutionMap.put(".7.3...6.....8.5...1.......8.96..4.....1.2...5...........7...324...9.............", "278359164694281573315467298829635417763142985541978326956714832432896751187523649");
        solutionMap.put("56..2......3...9...............7..561......2...84........3.84..71..........9.....", "561729348423681975897534261942173856135896724678452139256318497719245683384967512");
        solutionMap.put(".9.3...2.....7.5...1.......7.86..4.....9.2...5...........1...634...8.............", "697315824384279516215468379728631495143952687569847231852194763431786952976523148");
        solutionMap.put("7.8.2...........913.........46..........3.7.....5......5.9.6......4...1.2.....8..", "798123645462758391315649287546297138921834756837561924153986472689472513274315869");
        solutionMap.put("7...3........5.6....4....9.2.....7.1...9.8......4.....53....2.....1...8..6.......", "796834125812759634354216897289365741145978362673421958538647219427193586961582473");
        solutionMap.put("..3...67.5.....3...4.......6..3......8......4...7....12......5.....98.......41...", "813425679569817342742936815621354987987162534435789261294673158176598423358241796");
        solutionMap.put("4.35...2.....16...7............895.....3..8..2...........4...7..9....6...1.......", "463598721928716453751243968376189542145372896289654317832465179597821634614937285");
        solutionMap.put(".2.3...6.....7.5...1.......7.86..4.....9.2...5...........1...394...8.............", "927315864384276591615498372798631425143952687562847913856124739431789256279563148");
        solutionMap.put("......41.9..3.....3...2.....48..7..........62.1.......5..2....6.7....8......9....", "827659413964318257351724698248167935793485162615932784589271346176543829432896571");
        solutionMap.put("6.....7.5.3.8................52.3.8.1.9.........4.....42...........9.1......7.6..", "698321745531847296274956813765213489149785362382469571426138957857692134913574628");
        solutionMap.put(".5.1.8.7.4..3.....2.........1.7...8.9.....4............3.....1.....4.2......5.6..", "356128974471369825289475163512794386967832451843516792634287519795641238128953647");
        solutionMap.put("...6..9.23.87.....4............95..17......8...........2..6.5.....4...3..1.......", "175638942398724165462951873846395721751246389239187654924863517587419236613572498");
        solutionMap.put("8.5.....2...4.1...3.........6.7..4..2...5...........6....38.....1....9...9.....7.", "845697312926431857371528694569713428284956731137842569752389146613274985498165273");
        solutionMap.put("...6.37...51...........2.......1..546..7............8.14.58....3.....2...........", "498653712251879436736142895927318654684795123513426987142587369375961248869234571");
        solutionMap.put("..1.....8...9..2.......3.......15.4..6....7..3............4..8572.6.....9........", "231576498576984213489123567892715346164832759357469821613247985725698134948351672");

        // Test one that is already solved
        solutionMap.put("798635421126974583453218679972586314564123897381497256617352948835749162249861735", "798635421126974583453218679972586314564123897381497256617352948835749162249861735");
    }

    @Test
    public void test() {
        for (SudokuSolverAlgorithm algorithm : SudokuSolverAlgorithm.values()) {
            validateSolutions(algorithm);
        }
    }

    protected void validateSolutions(SudokuSolverAlgorithm algorithm) {
        StopWatch stopWatch = new StopWatch();
        log.debug("Starting Algorithm: {}.", algorithm);
        stopWatch.start();

        solutionMap.entrySet().forEach(entry ->
                assertEquals(entry.getValue(), service.solve(algorithm.name(), entry.getKey()).getSudokuBoard().toString())
        );

        stopWatch.stop();
        log.debug("Total time with Algorithm ({}) = {}.", algorithm, stopWatch.getTime());
    }

    @Test
    public void testBoardGeneration() {
        for (SudokuDifficultyLevel level : SudokuDifficultyLevel.values()) {
            log.debug("Starting Board Generation for SudokuDifficultyLevel: {}.", level);
            SudokuBoard board = service.createBoard(level);
            assertEquals(level.getMissingCellCount(), board.getUnpopulatedCells().size());
            service.solve(board);
        }
    }

    @Test
    public void testSolveAsString() {
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, service.solveAsString(SudokuSolverAlgorithm.KUDOKU.name(), SudokuBoardTest.BOARD_STRING_UNSOLVED));
        assertEquals(SudokuBoardTest.BOARD_STRING_SOLVED, service.solveAsString(SudokuBoardTest.BOARD_STRING_UNSOLVED));
    }

}
