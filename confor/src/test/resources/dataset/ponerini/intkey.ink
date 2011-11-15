*COMMENT Intkey initialization file.

*SET IMAGEPATH images

*SET INFOPATH info

*FILE TAXA iitems

*FILE CHARACTERS ichars

*FILE INPUT toolbar.inp

*SET RBASE 1.2

*DISPLAY UNKNOWNS Off

*DISPLAY INAPPLICABLES Off

*COMMENT : DEFINE CHARACTERS "nomenclature" 1

*COMMENT : DEFINE CHARACTERS "habit" 2-5 13

*COMMENT : DEFINE CHARACTERS "vegetative form" 2-11

*COMMENT : DEFINE CHARACTERS "   culms (form)" 3-6

*COMMENT : DEFINE CHARACTERS "   leaves (form)" 7-11

*COMMENT : DEFINE CHARACTERS "   ligules" 10-11

*COMMENT : DEFINE CHARACTERS "reproductive organization" 12 23-24

*COMMENT : DEFINE CHARACTERS "inflorescence form" 13-24

*COMMENT : DEFINE CHARACTERS "femsterile spikelets" 25

*COMMENT : DEFINE CHARACTERS "femfertile spikelets" 26-63

*COMMENT : DEFINE CHARACTERS "   glumes" 32-38

*COMMENT : DEFINE CHARACTERS "   incomplete florets" 39-43

*COMMENT : DEFINE CHARACTERS "   florets (female-fertile)" 44-63

*COMMENT : DEFINE CHARACTERS "   lemmas (female-fertile)" 45-54

*COMMENT : DEFINE CHARACTERS "   awns of female-fertile lemmas" 47-51

*COMMENT : DEFINE CHARACTERS "   paleas (female-fertile)" 55-56

*COMMENT : DEFINE CHARACTERS "   lodicules (female-fertile florets" 57-59

*COMMENT : DEFINE CHARACTERS "   androecium of female-fertile florets" 60

*COMMENT : DEFINE CHARACTERS "   gynoecium" 61-63

*COMMENT : DEFINE CHARACTERS "fruit" 64-67

*COMMENT : DEFINE CHARACTERS "photosynthetic pathway-related features" 68-70

*COMMENT : DEFINE CHARACTERS "   biochemistry" 69

*COMMENT : DEFINE CHARACTERS "ts anatomy of the leaf blade" 68 70-76

*COMMENT : DEFINE CHARACTERS "diagnostic features of individual taxa" 77

*COMMENT : DEFINE CHARACTERS "classification" 78-84

*COMMENT : DEFINE CHARACTERS "   subfamilies and supertribes" 78-79

*COMMENT : DEFINE CHARACTERS "   tribes" 80-84

*COMMENT : DEFINE CHARACTERS "species number" 85

*COMMENT : DEFINE CHARACTERS "geography" 86

*COMMENT : DEFINE CHARACTERS "references" 87

*COMMENT : DEFINE CHARACTERS "morphology" veg repro infl femster femfert glumes
incompl
florets lemmas paleas lodicu androec gynoec fruit diag

*COMMENT : DEFINE NAMES cereals Echinochloa, Eleusine, Oryza, Panicum, Zea

*COMMENT DELETE the following command if you want to use full descriptions
  generated by 'tonatsr'. See also 'toint', 'ofiles'.

*DEFINE INFORMATION "Full description" "describe ?S /c all"

*COMMENT : DEFINE CHARACTERS "brief description" nom class spec geog ref

*COMMENT : DEFINE INFORMATION "Brief description" "describe ?S /c brief"

*DEFINE INFORMATION "Diagnostic description" "diagnose ?S none"
