package edu.graal.utils;

import edu.graal.graphs.Graphlet;
import edu.graal.graphs.ImmutableGraphlet;
import edu.graal.graphs.types.GraphletEdge;
import edu.graal.graphs.types.GraphletVertex;
import edu.graal.graphs.types.ImmutableGraphletEdge;
import edu.graal.graphs.types.ImmutableGraphletVertex;
import edu.immutablessupport.styles.SingletonStyle;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import org.immutables.value.Value;

import java.util.function.Predicate;

@Value.Immutable
@SingletonStyle
public abstract class GraphletsUtil {
    private static final Array<Tuple2<Graphlet, Array<GraphletVertex>>> GRAPHLETS_AND_ORBIT_VERTICES = init();

    private static Array<Tuple2<Graphlet, Array<GraphletVertex>>> init() {
        Graphlet gZero = ImmutableGraphlet.of();
        GraphletVertex v000 = ImmutableGraphletVertex.of(0, 0);
        GraphletVertex v010 = ImmutableGraphletVertex.of(1, 0);
        GraphletEdge e0000010 = ImmutableGraphletEdge.of(v000, v010);
        gZero.addEdge(e0000010);

        Graphlet gOne = ImmutableGraphlet.of();
        GraphletVertex v101 = ImmutableGraphletVertex.of(0, 1);
        GraphletVertex v112 = ImmutableGraphletVertex.of(1, 2);
        GraphletVertex v121 = ImmutableGraphletVertex.of(2, 1);
        GraphletEdge e1101112 = ImmutableGraphletEdge.of(v101, v112);
        GraphletEdge e1112121 = ImmutableGraphletEdge.of(v112, v121);
        gOne.addEdge(e1101112);
        gOne.addEdge(e1112121);

        Graphlet gTwo = ImmutableGraphlet.of();
        GraphletVertex v203 = ImmutableGraphletVertex.of(0, 3);
        GraphletVertex v213 = ImmutableGraphletVertex.of(1, 3);
        GraphletVertex v223 = ImmutableGraphletVertex.of(2, 3);
        GraphletEdge e2203213 = ImmutableGraphletEdge.of(v203, v213);
        GraphletEdge e2203223 = ImmutableGraphletEdge.of(v203, v223);
        GraphletEdge e2213223 = ImmutableGraphletEdge.of(v213, v223);
        gTwo.addEdge(e2203213);
        gTwo.addEdge(e2203223);
        gTwo.addEdge(e2213223);

        Graphlet gThree = ImmutableGraphlet.of();
        GraphletVertex v304 = ImmutableGraphletVertex.of(0, 4);
        GraphletVertex v315 = ImmutableGraphletVertex.of(1, 5);
        GraphletVertex v325 = ImmutableGraphletVertex.of(2, 5);
        GraphletVertex v334 = ImmutableGraphletVertex.of(3, 4);
        GraphletEdge e3304315 = ImmutableGraphletEdge.of(v304, v315);
        GraphletEdge e3315325 = ImmutableGraphletEdge.of(v315, v325);
        GraphletEdge e3325334 = ImmutableGraphletEdge.of(v325, v334);
        gThree.addEdge(e3304315);
        gThree.addEdge(e3315325);
        gThree.addEdge(e3325334);

        Graphlet gFour = ImmutableGraphlet.of();
        GraphletVertex v406 = ImmutableGraphletVertex.of(0, 6);
        GraphletVertex v417 = ImmutableGraphletVertex.of(1, 7);
        GraphletVertex v426 = ImmutableGraphletVertex.of(2, 6);
        GraphletVertex v436 = ImmutableGraphletVertex.of(3, 6);
        GraphletEdge e4406417 = ImmutableGraphletEdge.of(v406, v417);
        GraphletEdge e4417426 = ImmutableGraphletEdge.of(v417, v426);
        GraphletEdge e4417436 = ImmutableGraphletEdge.of(v417, v436);
        gFour.addEdge(e4406417);
        gFour.addEdge(e4417426);
        gFour.addEdge(e4417436);

        Graphlet gFive = ImmutableGraphlet.of();
        GraphletVertex v508 = ImmutableGraphletVertex.of(0, 8);
        GraphletVertex v518 = ImmutableGraphletVertex.of(1, 8);
        GraphletVertex v528 = ImmutableGraphletVertex.of(2, 8);
        GraphletVertex v538 = ImmutableGraphletVertex.of(3, 8);
        GraphletEdge e5508518 = ImmutableGraphletEdge.of(v508, v518);
        GraphletEdge e5508528 = ImmutableGraphletEdge.of(v508, v528);
        GraphletEdge e5528538 = ImmutableGraphletEdge.of(v528, v538);
        GraphletEdge e5518538 = ImmutableGraphletEdge.of(v518, v538);
        gFive.addEdge(e5508518);
        gFive.addEdge(e5508528);
        gFive.addEdge(e5528538);
        gFive.addEdge(e5518538);

        Graphlet gSix = ImmutableGraphlet.of();
        GraphletVertex v6010 = ImmutableGraphletVertex.of(0, 10);
        GraphletVertex v6110 = ImmutableGraphletVertex.of(1, 10);
        GraphletVertex v6211 = ImmutableGraphletVertex.of(2, 11);
        GraphletVertex v639 = ImmutableGraphletVertex.of(3, 9);
        GraphletEdge e660106110 = ImmutableGraphletEdge.of(v6010, v6110);
        GraphletEdge e660106211 = ImmutableGraphletEdge.of(v6010, v6211);
        GraphletEdge e661106211 = ImmutableGraphletEdge.of(v6110, v6211);
        GraphletEdge e66211639 = ImmutableGraphletEdge.of(v6211, v639);
        gSix.addEdge(e660106110);
        gSix.addEdge(e660106211);
        gSix.addEdge(e661106211);
        gSix.addEdge(e66211639);

        Graphlet gSeven = ImmutableGraphlet.of();
        GraphletVertex v7013 = ImmutableGraphletVertex.of(0, 13);
        GraphletVertex v7112 = ImmutableGraphletVertex.of(1, 12);
        GraphletVertex v7212 = ImmutableGraphletVertex.of(2, 12);
        GraphletVertex v7313 = ImmutableGraphletVertex.of(3, 13);
        GraphletEdge e770137112 = ImmutableGraphletEdge.of(v7013, v7112);
        GraphletEdge e770137212 = ImmutableGraphletEdge.of(v7013, v7212);
        GraphletEdge e771127313 = ImmutableGraphletEdge.of(v7112, v7313);
        GraphletEdge e772127313 = ImmutableGraphletEdge.of(v7212, v7313);
        GraphletEdge e770137313 = ImmutableGraphletEdge.of(v7013, v7313);
        gSeven.addEdge(e770137112);
        gSeven.addEdge(e770137212);
        gSeven.addEdge(e771127313);
        gSeven.addEdge(e772127313);
        gSeven.addEdge(e770137313);

        Graphlet gEight = ImmutableGraphlet.of();
        GraphletVertex v8014 = ImmutableGraphletVertex.of(0, 14);
        GraphletVertex v8114 = ImmutableGraphletVertex.of(1, 14);
        GraphletVertex v8214 = ImmutableGraphletVertex.of(2, 14);
        GraphletVertex v8314 = ImmutableGraphletVertex.of(3, 14);
        GraphletEdge e880148114 = ImmutableGraphletEdge.of(v8014, v8114);
        GraphletEdge e880148214 = ImmutableGraphletEdge.of(v8014, v8214);
        GraphletEdge e880148314 = ImmutableGraphletEdge.of(v8014, v8314);
        GraphletEdge e881148214 = ImmutableGraphletEdge.of(v8114, v8214);
        GraphletEdge e881148314 = ImmutableGraphletEdge.of(v8114, v8314);
        GraphletEdge e882148314 = ImmutableGraphletEdge.of(v8214, v8314);
        gEight.addEdge(e880148114);
        gEight.addEdge(e880148214);
        gEight.addEdge(e880148314);
        gEight.addEdge(e881148214);
        gEight.addEdge(e881148314);
        gEight.addEdge(e882148314);

        Graphlet gNine = ImmutableGraphlet.of();
        GraphletVertex v9015 = ImmutableGraphletVertex.of(0, 15);
        GraphletVertex v9116 = ImmutableGraphletVertex.of(1, 16);
        GraphletVertex v9217 = ImmutableGraphletVertex.of(2, 17);
        GraphletVertex v9316 = ImmutableGraphletVertex.of(3, 16);
        GraphletVertex v9415 = ImmutableGraphletVertex.of(4, 15);
        GraphletEdge e990159116 = ImmutableGraphletEdge.of(v9015, v9116);
        GraphletEdge e991169217 = ImmutableGraphletEdge.of(v9116, v9217);
        GraphletEdge e992179316 = ImmutableGraphletEdge.of(v9217, v9316);
        GraphletEdge e993169415 = ImmutableGraphletEdge.of(v9316, v9415);
        gNine.addEdge(e990159116);
        gNine.addEdge(e991169217);
        gNine.addEdge(e992179316);
        gNine.addEdge(e993169415);

        Graphlet gTen = ImmutableGraphlet.of();
        GraphletVertex v10018 = ImmutableGraphletVertex.of(0, 18);
        GraphletVertex v10120 = ImmutableGraphletVertex.of(1, 20);
        GraphletVertex v10221 = ImmutableGraphletVertex.of(2, 21);
        GraphletVertex v10319 = ImmutableGraphletVertex.of(3, 19);
        GraphletVertex v10419 = ImmutableGraphletVertex.of(4, 19);
        GraphletEdge e101001810120 = ImmutableGraphletEdge.of(v10018, v10120);
        GraphletEdge e101012010221 = ImmutableGraphletEdge.of(v10120, v10221);
        GraphletEdge e101022110319 = ImmutableGraphletEdge.of(v10221, v10319);
        GraphletEdge e101022110419 = ImmutableGraphletEdge.of(v10221, v10419);
        gTen.addEdge(e101001810120);
        gTen.addEdge(e101012010221);
        gTen.addEdge(e101022110319);
        gTen.addEdge(e101022110419);

        Graphlet gEleven = ImmutableGraphlet.of();
        GraphletVertex v11022 = ImmutableGraphletVertex.of(0, 22);
        GraphletVertex v11122 = ImmutableGraphletVertex.of(1, 22);
        GraphletVertex v11222 = ImmutableGraphletVertex.of(2, 22);
        GraphletVertex v11322 = ImmutableGraphletVertex.of(3, 22);
        GraphletVertex v11423 = ImmutableGraphletVertex.of(4, 23);
        GraphletEdge e111102211423 = ImmutableGraphletEdge.of(v11022, v11423);
        GraphletEdge e111112211423 = ImmutableGraphletEdge.of(v11122, v11423);
        GraphletEdge e111122211423 = ImmutableGraphletEdge.of(v11222, v11423);
        GraphletEdge e111132211423 = ImmutableGraphletEdge.of(v11322, v11423);
        gEleven.addEdge(e111102211423);
        gEleven.addEdge(e111112211423);
        gEleven.addEdge(e111122211423);
        gEleven.addEdge(e111132211423);

        Graphlet gTwelve = ImmutableGraphlet.of();
        GraphletVertex v12025 = ImmutableGraphletVertex.of(0, 25);
        GraphletVertex v12126 = ImmutableGraphletVertex.of(1, 26);
        GraphletVertex v12226 = ImmutableGraphletVertex.of(2, 26);
        GraphletVertex v12324 = ImmutableGraphletVertex.of(3, 24);
        GraphletVertex v12424 = ImmutableGraphletVertex.of(4, 24);
        GraphletEdge e121202512126 = ImmutableGraphletEdge.of(v12025, v12126);
        GraphletEdge e121202512226 = ImmutableGraphletEdge.of(v12025, v12226);
        GraphletEdge e121212412226 = ImmutableGraphletEdge.of(v12126, v12226);
        GraphletEdge e121212612324 = ImmutableGraphletEdge.of(v12126, v12324);
        GraphletEdge e121222612424 = ImmutableGraphletEdge.of(v12226, v12424);
        gTwelve.addEdge(e121202512126);
        gTwelve.addEdge(e121202512226);
        gTwelve.addEdge(e121212412226);
        gTwelve.addEdge(e121212612324);
        gTwelve.addEdge(e121222612424);

        Graphlet gThirteen = ImmutableGraphlet.of();
        GraphletVertex v13029 = ImmutableGraphletVertex.of(0, 29);
        GraphletVertex v13129 = ImmutableGraphletVertex.of(1, 29);
        GraphletVertex v13230 = ImmutableGraphletVertex.of(2, 30);
        GraphletVertex v13328 = ImmutableGraphletVertex.of(3, 28);
        GraphletVertex v13427 = ImmutableGraphletVertex.of(4, 27);
        GraphletEdge e131302913129 = ImmutableGraphletEdge.of(v13029, v13129);
        GraphletEdge e131302913230 = ImmutableGraphletEdge.of(v13029, v13230);
        GraphletEdge e131312913230 = ImmutableGraphletEdge.of(v13129, v13230);
        GraphletEdge e131323013328 = ImmutableGraphletEdge.of(v13230, v13328);
        GraphletEdge e131332813427 = ImmutableGraphletEdge.of(v13328, v13427);
        gThirteen.addEdge(e131302913129);
        gThirteen.addEdge(e131302913230);
        gThirteen.addEdge(e131312913230);
        gThirteen.addEdge(e131323013328);
        gThirteen.addEdge(e131332813427);

        Graphlet gFourteen = ImmutableGraphlet.of();
        GraphletVertex v14032 = ImmutableGraphletVertex.of(0, 32);
        GraphletVertex v14132 = ImmutableGraphletVertex.of(1, 32);
        GraphletVertex v14233 = ImmutableGraphletVertex.of(2, 33);
        GraphletVertex v14331 = ImmutableGraphletVertex.of(3, 31);
        GraphletVertex v14431 = ImmutableGraphletVertex.of(4, 31);
        GraphletEdge e141403214132 = ImmutableGraphletEdge.of(v14032, v14132);
        GraphletEdge e141403214233 = ImmutableGraphletEdge.of(v14032, v14233);
        GraphletEdge e141413214233 = ImmutableGraphletEdge.of(v14132, v14233);
        GraphletEdge e141423314331 = ImmutableGraphletEdge.of(v14233, v14331);
        GraphletEdge e141423314431 = ImmutableGraphletEdge.of(v14233, v14431);
        gFourteen.addEdge(e141403214132);
        gFourteen.addEdge(e141403214233);
        gFourteen.addEdge(e141413214233);
        gFourteen.addEdge(e141423314331);
        gFourteen.addEdge(e141423314431);

        Graphlet gFifteen = ImmutableGraphlet.of();
        GraphletVertex v15034 = ImmutableGraphletVertex.of(0, 34);
        GraphletVertex v15134 = ImmutableGraphletVertex.of(1, 34);
        GraphletVertex v15234 = ImmutableGraphletVertex.of(2, 34);
        GraphletVertex v15334 = ImmutableGraphletVertex.of(3, 34);
        GraphletVertex v15434 = ImmutableGraphletVertex.of(4, 34);
        GraphletEdge e151503415134 = ImmutableGraphletEdge.of(v15034, v15134);
        GraphletEdge e151503415234 = ImmutableGraphletEdge.of(v15034, v15234);
        GraphletEdge e151513415334 = ImmutableGraphletEdge.of(v15134, v15334);
        GraphletEdge e151523415434 = ImmutableGraphletEdge.of(v15234, v15434);
        GraphletEdge e151533415434 = ImmutableGraphletEdge.of(v15334, v15434);
        gFifteen.addEdge(e151503415134);
        gFifteen.addEdge(e151503415234);
        gFifteen.addEdge(e151513415334);
        gFifteen.addEdge(e151523415434);
        gFifteen.addEdge(e151533415434);

        Graphlet gSixteen = ImmutableGraphlet.of();
        GraphletVertex v16036 = ImmutableGraphletVertex.of(0, 36);
        GraphletVertex v16137 = ImmutableGraphletVertex.of(1, 37);
        GraphletVertex v16237 = ImmutableGraphletVertex.of(2, 37);
        GraphletVertex v16338 = ImmutableGraphletVertex.of(3, 38);
        GraphletVertex v16435 = ImmutableGraphletVertex.of(4, 35);
        GraphletEdge e161603616137 = ImmutableGraphletEdge.of(v16036, v16137);
        GraphletEdge e161603616237 = ImmutableGraphletEdge.of(v16036, v16237);
        GraphletEdge e161613716338 = ImmutableGraphletEdge.of(v16137, v16338);
        GraphletEdge e161623716338 = ImmutableGraphletEdge.of(v16237, v16338);
        GraphletEdge e161633816435 = ImmutableGraphletEdge.of(v16338, v16435);
        gSixteen.addEdge(e161603616137);
        gSixteen.addEdge(e161603616237);
        gSixteen.addEdge(e161613716338);
        gSixteen.addEdge(e161623716338);
        gSixteen.addEdge(e161633816435);

        Graphlet gSeventeen = ImmutableGraphlet.of();
        GraphletVertex v17041 = ImmutableGraphletVertex.of(0, 41);
        GraphletVertex v17140 = ImmutableGraphletVertex.of(1, 40);
        GraphletVertex v17240 = ImmutableGraphletVertex.of(2, 40);
        GraphletVertex v17342 = ImmutableGraphletVertex.of(3, 42);
        GraphletVertex v17439 = ImmutableGraphletVertex.of(4, 39);
        GraphletEdge e171704117140 = ImmutableGraphletEdge.of(v17041, v17140);
        GraphletEdge e171704117240 = ImmutableGraphletEdge.of(v17041, v17240);
        GraphletEdge e171704117342 = ImmutableGraphletEdge.of(v17041, v17342);
        GraphletEdge e171714017342 = ImmutableGraphletEdge.of(v17140, v17342);
        GraphletEdge e171724017342 = ImmutableGraphletEdge.of(v17240, v17342);
        GraphletEdge e171734217439 = ImmutableGraphletEdge.of(v17342, v17439);
        gSeventeen.addEdge(e171704117140);
        gSeventeen.addEdge(e171704117240);
        gSeventeen.addEdge(e171704117342);
        gSeventeen.addEdge(e171714017342);
        gSeventeen.addEdge(e171724017342);
        gSeventeen.addEdge(e171734217439);

        Graphlet gEighteen = ImmutableGraphlet.of();
        GraphletVertex v18043 = ImmutableGraphletVertex.of(0, 43);
        GraphletVertex v18143 = ImmutableGraphletVertex.of(1, 43);
        GraphletVertex v18244 = ImmutableGraphletVertex.of(2, 44);
        GraphletVertex v18343 = ImmutableGraphletVertex.of(3, 43);
        GraphletVertex v18443 = ImmutableGraphletVertex.of(4, 43);
        GraphletEdge e181804318143 = ImmutableGraphletEdge.of(v18043, v18143);
        GraphletEdge e181804318244 = ImmutableGraphletEdge.of(v18043, v18244);
        GraphletEdge e181814318244 = ImmutableGraphletEdge.of(v18143, v18244);
        GraphletEdge e181824418343 = ImmutableGraphletEdge.of(v18244, v18343);
        GraphletEdge e181824418443 = ImmutableGraphletEdge.of(v18244, v18443);
        GraphletEdge e181834318443 = ImmutableGraphletEdge.of(v18343, v18443);
        gEighteen.addEdge(e181804318143);
        gEighteen.addEdge(e181804318244);
        gEighteen.addEdge(e181814318244);
        gEighteen.addEdge(e181824418343);
        gEighteen.addEdge(e181824418443);
        gEighteen.addEdge(e181834318443);

        Graphlet gNineteen = ImmutableGraphlet.of();
        GraphletVertex v19046 = ImmutableGraphletVertex.of(0, 46);
        GraphletVertex v19148 = ImmutableGraphletVertex.of(1, 48);
        GraphletVertex v19248 = ImmutableGraphletVertex.of(2, 48);
        GraphletVertex v19347 = ImmutableGraphletVertex.of(3, 47);
        GraphletVertex v19445 = ImmutableGraphletVertex.of(4, 45);
        GraphletEdge e191904619148 = ImmutableGraphletEdge.of(v19046, v19148);
        GraphletEdge e191904619248 = ImmutableGraphletEdge.of(v19046, v19248);
        GraphletEdge e191914819248 = ImmutableGraphletEdge.of(v19148, v19248);
        GraphletEdge e191914819347 = ImmutableGraphletEdge.of(v19148, v19347);
        GraphletEdge e191924819347 = ImmutableGraphletEdge.of(v19248, v19347);
        GraphletEdge e191934719445 = ImmutableGraphletEdge.of(v19347, v19445);
        gNineteen.addEdge(e191904619148);
        gNineteen.addEdge(e191904619248);
        gNineteen.addEdge(e191914819248);
        gNineteen.addEdge(e191914819347);
        gNineteen.addEdge(e191924819347);
        gNineteen.addEdge(e191934719445);

        Graphlet gTwenty = ImmutableGraphlet.of();
        GraphletVertex v20050 = ImmutableGraphletVertex.of(0, 50);
        GraphletVertex v20149 = ImmutableGraphletVertex.of(1, 49);
        GraphletVertex v20249 = ImmutableGraphletVertex.of(2, 49);
        GraphletVertex v20349 = ImmutableGraphletVertex.of(3, 49);
        GraphletVertex v20450 = ImmutableGraphletVertex.of(4, 50);
        GraphletEdge e202005020149 = ImmutableGraphletEdge.of(v20050, v20149);
        GraphletEdge e202005020249 = ImmutableGraphletEdge.of(v20050, v20249);
        GraphletEdge e202005020349 = ImmutableGraphletEdge.of(v20050, v20349);
        GraphletEdge e202014920450 = ImmutableGraphletEdge.of(v20149, v20450);
        GraphletEdge e202024920450 = ImmutableGraphletEdge.of(v20249, v20450);
        GraphletEdge e202034920450 = ImmutableGraphletEdge.of(v20349, v20450);
        gTwenty.addEdge(e202005020149);
        gTwenty.addEdge(e202005020249);
        gTwenty.addEdge(e202005020349);
        gTwenty.addEdge(e202014920450);
        gTwenty.addEdge(e202024920450);
        gTwenty.addEdge(e202034920450);

        Graphlet gTwentyOne = ImmutableGraphlet.of();
        GraphletVertex v21052 = ImmutableGraphletVertex.of(0, 52);
        GraphletVertex v21153 = ImmutableGraphletVertex.of(1, 53);
        GraphletVertex v21253 = ImmutableGraphletVertex.of(2, 53);
        GraphletVertex v21351 = ImmutableGraphletVertex.of(3, 51);
        GraphletVertex v21451 = ImmutableGraphletVertex.of(4, 51);
        GraphletEdge e212105221153 = ImmutableGraphletEdge.of(v21052, v21153);
        GraphletEdge e212105221253 = ImmutableGraphletEdge.of(v21052, v21253);
        GraphletEdge e212115321253 = ImmutableGraphletEdge.of(v21153, v21253);
        GraphletEdge e212115321351 = ImmutableGraphletEdge.of(v21153, v21351);
        GraphletEdge e212125321451 = ImmutableGraphletEdge.of(v21253, v21451);
        GraphletEdge e212135121451 = ImmutableGraphletEdge.of(v21351, v21451);
        gTwentyOne.addEdge(e212105221153);
        gTwentyOne.addEdge(e212105221253);
        gTwentyOne.addEdge(e212115321253);
        gTwentyOne.addEdge(e212115321351);
        gTwentyOne.addEdge(e212125321451);
        gTwentyOne.addEdge(e212135121451);

        Graphlet gTwentyTwo = ImmutableGraphlet.of();
        GraphletVertex v22054 = ImmutableGraphletVertex.of(0, 54);
        GraphletVertex v22155 = ImmutableGraphletVertex.of(1, 55);
        GraphletVertex v22255 = ImmutableGraphletVertex.of(2, 55);
        GraphletVertex v22354 = ImmutableGraphletVertex.of(3, 54);
        GraphletVertex v22454 = ImmutableGraphletVertex.of(4, 54);
        GraphletEdge e222205422155 = ImmutableGraphletEdge.of(v22054, v22155);
        GraphletEdge e222205422255 = ImmutableGraphletEdge.of(v22054, v22255);
        GraphletEdge e222215522255 = ImmutableGraphletEdge.of(v22155, v22255);
        GraphletEdge e222215522354 = ImmutableGraphletEdge.of(v22155, v22354);
        GraphletEdge e222225522354 = ImmutableGraphletEdge.of(v22255, v22354);
        GraphletEdge e222215522454 = ImmutableGraphletEdge.of(v22155, v22454);
        GraphletEdge e222225522454 = ImmutableGraphletEdge.of(v22255, v22454);
        gTwentyTwo.addEdge(e222205422155);
        gTwentyTwo.addEdge(e222205422255);
        gTwentyTwo.addEdge(e222215522255);
        gTwentyTwo.addEdge(e222215522354);
        gTwentyTwo.addEdge(e222225522354);
        gTwentyTwo.addEdge(e222215522454);
        gTwentyTwo.addEdge(e222225522454);

        Graphlet gTwentyThree = ImmutableGraphlet.of();
        GraphletVertex v23057 = ImmutableGraphletVertex.of(0, 57);
        GraphletVertex v23157 = ImmutableGraphletVertex.of(1, 57);
        GraphletVertex v23258 = ImmutableGraphletVertex.of(2, 58);
        GraphletVertex v23357 = ImmutableGraphletVertex.of(3, 57);
        GraphletVertex v23456 = ImmutableGraphletVertex.of(4, 56);
        GraphletEdge e232305723157 = ImmutableGraphletEdge.of(v23057, v23157);
        GraphletEdge e232305723258 = ImmutableGraphletEdge.of(v23057, v23258);
        GraphletEdge e232305723357 = ImmutableGraphletEdge.of(v23057, v23357);
        GraphletEdge e232315723258 = ImmutableGraphletEdge.of(v23157, v23258);
        GraphletEdge e232315723357 = ImmutableGraphletEdge.of(v23157, v23357);
        GraphletEdge e232325823357 = ImmutableGraphletEdge.of(v23258, v23357);
        GraphletEdge e232325823456 = ImmutableGraphletEdge.of(v23258, v23456);
        gTwentyThree.addEdge(e232305723157);
        gTwentyThree.addEdge(e232305723258);
        gTwentyThree.addEdge(e232305723357);
        gTwentyThree.addEdge(e232315723258);
        gTwentyThree.addEdge(e232315723357);
        gTwentyThree.addEdge(e232325823357);
        gTwentyThree.addEdge(e232325823456);

        Graphlet gTwentyFour = ImmutableGraphlet.of();
        GraphletVertex v24059 = ImmutableGraphletVertex.of(0, 59);
        GraphletVertex v24160 = ImmutableGraphletVertex.of(1, 60);
        GraphletVertex v24261 = ImmutableGraphletVertex.of(2, 61);
        GraphletVertex v24360 = ImmutableGraphletVertex.of(3, 60);
        GraphletVertex v24459 = ImmutableGraphletVertex.of(4, 59);
        GraphletEdge e242405924160 = ImmutableGraphletEdge.of(v24059, v24160);
        GraphletEdge e242405924261 = ImmutableGraphletEdge.of(v24059, v24261);
        GraphletEdge e242416024261 = ImmutableGraphletEdge.of(v24160, v24261);
        GraphletEdge e242416024360 = ImmutableGraphletEdge.of(v24160, v24360);
        GraphletEdge e242426124360 = ImmutableGraphletEdge.of(v24261, v24360);
        GraphletEdge e242426124459 = ImmutableGraphletEdge.of(v24261, v24459);
        GraphletEdge e242436024459 = ImmutableGraphletEdge.of(v24360, v24459);
        gTwentyFour.addEdge(e242405924160);
        gTwentyFour.addEdge(e242405924261);
        gTwentyFour.addEdge(e242416024261);
        gTwentyFour.addEdge(e242416024360);
        gTwentyFour.addEdge(e242426124360);
        gTwentyFour.addEdge(e242426124459);
        gTwentyFour.addEdge(e242436024459);

        Graphlet gTwentyFive = ImmutableGraphlet.of();
        GraphletVertex v25063 = ImmutableGraphletVertex.of(0, 63);
        GraphletVertex v25164 = ImmutableGraphletVertex.of(1, 64);
        GraphletVertex v25264 = ImmutableGraphletVertex.of(2, 64);
        GraphletVertex v25362 = ImmutableGraphletVertex.of(3, 62);
        GraphletVertex v25463 = ImmutableGraphletVertex.of(4, 63);
        GraphletEdge e252506325164 = ImmutableGraphletEdge.of(v25063, v25164);
        GraphletEdge e252506325264 = ImmutableGraphletEdge.of(v25063, v25264);
        GraphletEdge e252506325362 = ImmutableGraphletEdge.of(v25063, v25362);
        GraphletEdge e252516425463 = ImmutableGraphletEdge.of(v25164, v25463);
        GraphletEdge e252516425264 = ImmutableGraphletEdge.of(v25164, v25264);
        GraphletEdge e252526425463 = ImmutableGraphletEdge.of(v25264, v25463);
        GraphletEdge e252536225463 = ImmutableGraphletEdge.of(v25362, v25463);
        gTwentyFive.addEdge(e252506325164);
        gTwentyFive.addEdge(e252506325264);
        gTwentyFive.addEdge(e252506325362);
        gTwentyFive.addEdge(e252516425463);
        gTwentyFive.addEdge(e252516425264);
        gTwentyFive.addEdge(e252526425463);
        gTwentyFive.addEdge(e252536225463);

        Graphlet gTwentySix = ImmutableGraphlet.of();
        GraphletVertex v26065 = ImmutableGraphletVertex.of(0, 65);
        GraphletVertex v26167 = ImmutableGraphletVertex.of(1, 67);
        GraphletVertex v26267 = ImmutableGraphletVertex.of(2, 67);
        GraphletVertex v26366 = ImmutableGraphletVertex.of(3, 66);
        GraphletVertex v26466 = ImmutableGraphletVertex.of(4, 66);
        GraphletEdge e262606526167 = ImmutableGraphletEdge.of(v26065, v26167);
        GraphletEdge e262606526267 = ImmutableGraphletEdge.of(v26065, v26267);
        GraphletEdge e262616726267 = ImmutableGraphletEdge.of(v26167, v26267);
        GraphletEdge e262616726366 = ImmutableGraphletEdge.of(v26167, v26366);
        GraphletEdge e262616726466 = ImmutableGraphletEdge.of(v26167, v26466);
        GraphletEdge e262626726366 = ImmutableGraphletEdge.of(v26267, v26366);
        GraphletEdge e262626726466 = ImmutableGraphletEdge.of(v26267, v26466);
        GraphletEdge e262636626466 = ImmutableGraphletEdge.of(v26366, v26466);
        gTwentySix.addEdge(e262606526167);
        gTwentySix.addEdge(e262606526267);
        gTwentySix.addEdge(e262616726267);
        gTwentySix.addEdge(e262616726366);
        gTwentySix.addEdge(e262616726466);
        gTwentySix.addEdge(e262626726366);
        gTwentySix.addEdge(e262626726466);
        gTwentySix.addEdge(e262636626466);

        Graphlet gTwentySeven = ImmutableGraphlet.of();
        GraphletVertex v27068 = ImmutableGraphletVertex.of(0, 68);
        GraphletVertex v27168 = ImmutableGraphletVertex.of(1, 68);
        GraphletVertex v27269 = ImmutableGraphletVertex.of(2, 69);
        GraphletVertex v27368 = ImmutableGraphletVertex.of(3, 68);
        GraphletVertex v27468 = ImmutableGraphletVertex.of(4, 68);
        GraphletEdge e272706827168 = ImmutableGraphletEdge.of(v27068, v27168);
        GraphletEdge e272706827269 = ImmutableGraphletEdge.of(v27068, v27269);
        GraphletEdge e272706827368 = ImmutableGraphletEdge.of(v27068, v27368);
        GraphletEdge e272716827269 = ImmutableGraphletEdge.of(v27168, v27269);
        GraphletEdge e272716827468 = ImmutableGraphletEdge.of(v27168, v27468);
        GraphletEdge e272726927368 = ImmutableGraphletEdge.of(v27269, v27368);
        GraphletEdge e272726927468 = ImmutableGraphletEdge.of(v27269, v27468);
        GraphletEdge e272736827468 = ImmutableGraphletEdge.of(v27368, v27468);
        gTwentySeven.addEdge(e272706827168);
        gTwentySeven.addEdge(e272706827269);
        gTwentySeven.addEdge(e272706827368);
        gTwentySeven.addEdge(e272716827269);
        gTwentySeven.addEdge(e272716827468);
        gTwentySeven.addEdge(e272726927368);
        gTwentySeven.addEdge(e272726927468);
        gTwentySeven.addEdge(e272736827468);

        Graphlet gTwentyEight = ImmutableGraphlet.of();
        GraphletVertex v28070 = ImmutableGraphletVertex.of(0, 70);
        GraphletVertex v28171 = ImmutableGraphletVertex.of(1, 71);
        GraphletVertex v28271 = ImmutableGraphletVertex.of(2, 71);
        GraphletVertex v28371 = ImmutableGraphletVertex.of(3, 71);
        GraphletVertex v28470 = ImmutableGraphletVertex.of(4, 70);
        GraphletEdge e282807028171 = ImmutableGraphletEdge.of(v28070, v28171);
        GraphletEdge e282807028271 = ImmutableGraphletEdge.of(v28070, v28271);
        GraphletEdge e282807028371 = ImmutableGraphletEdge.of(v28070, v28371);
        GraphletEdge e282817128271 = ImmutableGraphletEdge.of(v28171, v28271);
        GraphletEdge e282817128371 = ImmutableGraphletEdge.of(v28171, v28371);
        GraphletEdge e282817128470 = ImmutableGraphletEdge.of(v28171, v28470);
        GraphletEdge e282837128470 = ImmutableGraphletEdge.of(v28371, v28470);
        GraphletEdge e282827128371 = ImmutableGraphletEdge.of(v28271, v28371);
        GraphletEdge e282827128470 = ImmutableGraphletEdge.of(v28271, v28470);
        gTwentyEight.addEdge(e282807028171);
        gTwentyEight.addEdge(e282807028271);
        gTwentyEight.addEdge(e282807028371);
        gTwentyEight.addEdge(e282817128271);
        gTwentyEight.addEdge(e282817128371);
        gTwentyEight.addEdge(e282817128470);
        gTwentyEight.addEdge(e282837128470);
        gTwentyEight.addEdge(e282827128371);
        gTwentyEight.addEdge(e282827128470);

        Graphlet gTwentyNine = ImmutableGraphlet.of();
        GraphletVertex v29072 = ImmutableGraphletVertex.of(0, 72);
        GraphletVertex v29172 = ImmutableGraphletVertex.of(1, 72);
        GraphletVertex v29272 = ImmutableGraphletVertex.of(2, 72);
        GraphletVertex v29372 = ImmutableGraphletVertex.of(3, 72);
        GraphletVertex v29472 = ImmutableGraphletVertex.of(4, 72);
        GraphletEdge e292907229172 = ImmutableGraphletEdge.of(v29072, v29172);
        GraphletEdge e292907229272 = ImmutableGraphletEdge.of(v29072, v29272);
        GraphletEdge e292907229372 = ImmutableGraphletEdge.of(v29072, v29372);
        GraphletEdge e292907229472 = ImmutableGraphletEdge.of(v29072, v29472);
        GraphletEdge e292917229272 = ImmutableGraphletEdge.of(v29172, v29272);
        GraphletEdge e292917229372 = ImmutableGraphletEdge.of(v29172, v29372);
        GraphletEdge e292917229472 = ImmutableGraphletEdge.of(v29172, v29472);
        GraphletEdge e292927229372 = ImmutableGraphletEdge.of(v29272, v29372);
        GraphletEdge e292927329472 = ImmutableGraphletEdge.of(v29272, v29472);
        GraphletEdge e292937329472 = ImmutableGraphletEdge.of(v29372, v29472);
        gTwentyNine.addEdge(e292907229172);
        gTwentyNine.addEdge(e292907229272);
        gTwentyNine.addEdge(e292907229372);
        gTwentyNine.addEdge(e292907229472);
        gTwentyNine.addEdge(e292917229272);
        gTwentyNine.addEdge(e292917229372);
        gTwentyNine.addEdge(e292917229472);
        gTwentyNine.addEdge(e292927229372);
        gTwentyNine.addEdge(e292927329472);
        gTwentyNine.addEdge(e292937329472);

        return Array.of(
                Tuple.of(gZero, Array.of(v000)),
                Tuple.of(gOne, Array.of(v101, v112)),
                Tuple.of(gTwo, Array.of(v203)),
                Tuple.of(gThree, Array.of(v304, v315)),
                Tuple.of(gFour, Array.of(v406, v417)),
                Tuple.of(gFive, Array.of(v508)),
                Tuple.of(gSix, Array.of(v639, v6010, v6211)),
                Tuple.of(gSeven, Array.of(v7112, v7013)),
                Tuple.of(gEight, Array.of(v8014)),
                Tuple.of(gNine, Array.of(v9015, v9116, v9217)),
                Tuple.of(gTen, Array.of(v10018, v10319, v10120, v10221)),
                Tuple.of(gEleven, Array.of(v11022, v11423)),
                Tuple.of(gTwelve, Array.of(v12424, v12025, v12126)),
                Tuple.of(gThirteen, Array.of(v13427, v13328, v13029, v13230)),
                Tuple.of(gFourteen, Array.of(v14331, v14032, v14233)),
                Tuple.of(gFifteen, Array.of(v15034)),
                Tuple.of(gSixteen, Array.of(v16435, v16036, v16137, v16338)),
                Tuple.of(gSeventeen, Array.of(v17439, v17140, v17041, v17342)),
                Tuple.of(gEighteen, Array.of(v18043, v18244)),
                Tuple.of(gNineteen, Array.of(v19445, v19046, v19347, v19148)),
                Tuple.of(gTwenty, Array.of(v20149, v20050)),
                Tuple.of(gTwentyOne, Array.of(v21351, v21052, v21153)),
                Tuple.of(gTwentyTwo, Array.of(v22054, v22155)),
                Tuple.of(gTwentyThree, Array.of(v23456, v23057, v23258)),
                Tuple.of(gTwentyFour, Array.of(v24059, v24160, v24261)),
                Tuple.of(gTwentyFive, Array.of(v25362, v25063, v25164)),
                Tuple.of(gTwentySix, Array.of(v26065, v26366, v26167)),
                Tuple.of(gTwentySeven, Array.of(v27068, v27269)),
                Tuple.of(gTwentyEight, Array.of(v28070, v28171)),
                Tuple.of(gTwentyNine, Array.of(v29072))
        );
    }

    @Value.Lazy
    public Array<Graphlet> getGraphlets() {
        return GRAPHLETS_AND_ORBIT_VERTICES.map(tuple -> tuple._1);
    }

    @Value.Lazy
    public Array<GraphletVertex> getOrbitVertices() {
        return GRAPHLETS_AND_ORBIT_VERTICES.flatMap(tuple -> tuple._2);
    }

    /**
     * Orbit weights are calculated only once and served from cache for subsequent calls (@see ImmutableGraphletsUtil)
     */
    @Value.Lazy
    public Array<Double> getOrbitsWeight() {
        SignatureProvider signatureProvider = ImmutableSignatureProvider.of();
        Predicate<Long> notZero = l -> l != 0;

        return GRAPHLETS_AND_ORBIT_VERTICES
                .flatMap(tuple -> tuple._2.map(orbitVertex -> Tuple.of(tuple._1, orbitVertex)))
                .map(tuple -> signatureProvider.getSignature(tuple._1.getAsUndirectedGraph(), tuple._2))
                .map(signature -> Array.ofAll(signature).filter(notZero).size())
                .map(orbitCount -> 1 - (Math.log(orbitCount) / Math.log(73)));
    }

    @Value.Lazy
    public double getTotalOrbitsWeight() {
        return getOrbitsWeight().sum().doubleValue();
    }

}