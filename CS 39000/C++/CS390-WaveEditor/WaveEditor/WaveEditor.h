
// WaveEditor.h : main header file for the WaveEditor application
//
#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"       // main symbols
#include "WaveFile.h"


// CWaveEditorApp:
// See WaveEditor.cpp for the implementation of this class
//

class CWaveEditorApp : public CWinApp
{
public:
	CWaveEditorApp();
	WaveFile * clipboard;

// Overrides
public:
	virtual BOOL InitInstance();

// Implementation
	afx_msg void OnAppAbout();
	DECLARE_MESSAGE_MAP()
};

extern CWaveEditorApp theApp;
