# LLVM CMake target exports.  Do not include directly.
add_library(LLVMLTO STATIC IMPORTED)
set_property(TARGET LLVMLTO PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMLTO.a")
add_library(LLVMObjCARCOpts STATIC IMPORTED)
set_property(TARGET LLVMObjCARCOpts PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMObjCARCOpts.a")
add_library(LLVMLinker STATIC IMPORTED)
set_property(TARGET LLVMLinker PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMLinker.a")
add_library(LLVMipo STATIC IMPORTED)
set_property(TARGET LLVMipo PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMipo.a")
add_library(LLVMVectorize STATIC IMPORTED)
set_property(TARGET LLVMVectorize PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMVectorize.a")
add_library(LLVMBitWriter STATIC IMPORTED)
set_property(TARGET LLVMBitWriter PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMBitWriter.a")
add_library(LLVMTableGen STATIC IMPORTED)
set_property(TARGET LLVMTableGen PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMTableGen.a")
add_library(LLVMDebugInfo STATIC IMPORTED)
set_property(TARGET LLVMDebugInfo PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMDebugInfo.a")
add_library(LLVMOption STATIC IMPORTED)
set_property(TARGET LLVMOption PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMOption.a")
add_library(LLVMX86Disassembler STATIC IMPORTED)
set_property(TARGET LLVMX86Disassembler PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86Disassembler.a")
add_library(LLVMX86AsmParser STATIC IMPORTED)
set_property(TARGET LLVMX86AsmParser PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86AsmParser.a")
add_library(LLVMX86CodeGen STATIC IMPORTED)
set_property(TARGET LLVMX86CodeGen PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86CodeGen.a")
add_library(LLVMSelectionDAG STATIC IMPORTED)
set_property(TARGET LLVMSelectionDAG PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMSelectionDAG.a")
add_library(LLVMAsmPrinter STATIC IMPORTED)
set_property(TARGET LLVMAsmPrinter PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMAsmPrinter.a")
add_library(LLVMX86Desc STATIC IMPORTED)
set_property(TARGET LLVMX86Desc PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86Desc.a")
add_library(LLVMMCDisassembler STATIC IMPORTED)
set_property(TARGET LLVMMCDisassembler PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMMCDisassembler.a")
add_library(LLVMX86Info STATIC IMPORTED)
set_property(TARGET LLVMX86Info PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86Info.a")
add_library(LLVMX86AsmPrinter STATIC IMPORTED)
set_property(TARGET LLVMX86AsmPrinter PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86AsmPrinter.a")
add_library(LLVMX86Utils STATIC IMPORTED)
set_property(TARGET LLVMX86Utils PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMX86Utils.a")
add_library(LLVMJIT STATIC IMPORTED)
set_property(TARGET LLVMJIT PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMJIT.a")
add_library(LLVMIRReader STATIC IMPORTED)
set_property(TARGET LLVMIRReader PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMIRReader.a")
add_library(LLVMAsmParser STATIC IMPORTED)
set_property(TARGET LLVMAsmParser PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMAsmParser.a")
add_library(LLVMLineEditor STATIC IMPORTED)
set_property(TARGET LLVMLineEditor PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMLineEditor.a")
add_library(LLVMMCAnalysis STATIC IMPORTED)
set_property(TARGET LLVMMCAnalysis PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMMCAnalysis.a")
add_library(LLVMInstrumentation STATIC IMPORTED)
set_property(TARGET LLVMInstrumentation PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMInstrumentation.a")
add_library(LLVMInterpreter STATIC IMPORTED)
set_property(TARGET LLVMInterpreter PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMInterpreter.a")
add_library(LLVMCodeGen STATIC IMPORTED)
set_property(TARGET LLVMCodeGen PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMCodeGen.a")
add_library(LLVMScalarOpts STATIC IMPORTED)
set_property(TARGET LLVMScalarOpts PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMScalarOpts.a")
add_library(LLVMInstCombine STATIC IMPORTED)
set_property(TARGET LLVMInstCombine PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMInstCombine.a")
add_library(LLVMTransformUtils STATIC IMPORTED)
set_property(TARGET LLVMTransformUtils PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMTransformUtils.a")
add_library(LLVMipa STATIC IMPORTED)
set_property(TARGET LLVMipa PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMipa.a")
add_library(LLVMAnalysis STATIC IMPORTED)
set_property(TARGET LLVMAnalysis PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMAnalysis.a")
add_library(LLVMProfileData STATIC IMPORTED)
set_property(TARGET LLVMProfileData PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMProfileData.a")
add_library(LLVMMCJIT STATIC IMPORTED)
set_property(TARGET LLVMMCJIT PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMMCJIT.a")
add_library(LLVMTarget STATIC IMPORTED)
set_property(TARGET LLVMTarget PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMTarget.a")
add_library(LLVMRuntimeDyld STATIC IMPORTED)
set_property(TARGET LLVMRuntimeDyld PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMRuntimeDyld.a")
add_library(LLVMObject STATIC IMPORTED)
set_property(TARGET LLVMObject PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMObject.a")
add_library(LLVMMCParser STATIC IMPORTED)
set_property(TARGET LLVMMCParser PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMMCParser.a")
add_library(LLVMBitReader STATIC IMPORTED)
set_property(TARGET LLVMBitReader PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMBitReader.a")
add_library(LLVMExecutionEngine STATIC IMPORTED)
set_property(TARGET LLVMExecutionEngine PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMExecutionEngine.a")
add_library(LLVMMC STATIC IMPORTED)
set_property(TARGET LLVMMC PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMMC.a")
add_library(LLVMCore STATIC IMPORTED)
set_property(TARGET LLVMCore PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMCore.a")
add_library(LLVMSupport STATIC IMPORTED)
set_property(TARGET LLVMSupport PROPERTY IMPORTED_LOCATION "/ec/build/cling-4f217d95b3d8-full-1922637/cling-Ubuntu-14.04-64bit-4f217d95b3/lib/libLLVMSupport.a")
# Explicit library dependency information.
#
# The following property assignments tell CMake about link
# dependencies of libraries imported from LLVM.
set_property(TARGET LLVMSupport PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES )
set_property(TARGET LLVMMC PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMMCParser PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMSupport)
set_property(TARGET LLVMCore PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMTarget PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMMC LLVMSupport)
set_property(TARGET LLVMAnalysis PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport LLVMTarget)
set_property(TARGET LLVMipa PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport)
set_property(TARGET LLVMTransformUtils PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport LLVMTarget LLVMipa)
set_property(TARGET LLVMInstCombine PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMScalarOpts PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMInstCombine LLVMSupport LLVMTarget LLVMTransformUtils LLVMipa)
set_property(TARGET LLVMCodeGen PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMMC LLVMScalarOpts LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMAsmPrinter PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCodeGen LLVMCore LLVMMC LLVMMCParser LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMSelectionDAG PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCodeGen LLVMCore LLVMMC LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMMCDisassembler PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMSupport)
set_property(TARGET LLVMAsmParser PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport)
set_property(TARGET LLVMBitReader PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport)
set_property(TARGET LLVMBitWriter PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport)
set_property(TARGET LLVMObject PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMBitReader LLVMCore LLVMMC LLVMMCParser LLVMSupport)
set_property(TARGET LLVMDebugInfo PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMObject LLVMSupport)
set_property(TARGET LLVMExecutionEngine PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMMC LLVMSupport)
set_property(TARGET LLVMJIT PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCodeGen LLVMCore LLVMExecutionEngine LLVMSupport)
set_property(TARGET LLVMVectorize PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMipo PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMInstCombine LLVMScalarOpts LLVMSupport LLVMTarget LLVMTransformUtils LLVMVectorize LLVMipa)
set_property(TARGET LLVMIRReader PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAsmParser LLVMBitReader LLVMCore LLVMSupport)
set_property(TARGET LLVMInstrumentation PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport LLVMTarget LLVMTransformUtils)
set_property(TARGET LLVMInterpreter PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCodeGen LLVMCore LLVMExecutionEngine LLVMSupport)
set_property(TARGET LLVMLinker PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport LLVMTransformUtils)
set_property(TARGET LLVMObjCARCOpts PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMCore LLVMSupport LLVMTransformUtils)
set_property(TARGET LLVMLTO PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMBitReader LLVMBitWriter LLVMCodeGen LLVMCore LLVMInstCombine LLVMLinker LLVMMC LLVMObjCARCOpts LLVMObject LLVMScalarOpts LLVMSupport LLVMTarget LLVMTransformUtils LLVMipa LLVMipo)
set_property(TARGET LLVMLineEditor PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMMCAnalysis PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMObject LLVMSupport)
set_property(TARGET LLVMRuntimeDyld PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMObject LLVMSupport)
set_property(TARGET LLVMMCJIT PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMExecutionEngine LLVMObject LLVMRuntimeDyld LLVMSupport LLVMTarget)
set_property(TARGET LLVMX86Utils PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMCore LLVMSupport)
set_property(TARGET LLVMX86AsmPrinter PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMSupport LLVMX86Utils)
set_property(TARGET LLVMX86Info PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMX86Desc PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMMCDisassembler LLVMObject LLVMSupport LLVMX86AsmPrinter LLVMX86Info)
set_property(TARGET LLVMX86CodeGen PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMAnalysis LLVMAsmPrinter LLVMCodeGen LLVMCore LLVMMC LLVMSelectionDAG LLVMSupport LLVMTarget LLVMX86AsmPrinter LLVMX86Desc LLVMX86Info LLVMX86Utils)
set_property(TARGET LLVMOption PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMProfileData PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMObject LLVMSupport)
set_property(TARGET LLVMTableGen PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMSupport)
set_property(TARGET LLVMX86AsmParser PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMC LLVMMCParser LLVMSupport LLVMX86Desc LLVMX86Info)
set_property(TARGET LLVMX86Disassembler PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES LLVMMCDisassembler LLVMSupport LLVMX86Info)
set_property(TARGET LLVMSupport APPEND PROPERTY IMPORTED_LINK_INTERFACE_LIBRARIES z pthread dl m )
