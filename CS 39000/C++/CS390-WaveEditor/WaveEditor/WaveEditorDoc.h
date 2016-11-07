
// WaveEditDoc.h : interface of the CWaveEditDoc class
//


#pragma once

#include "WaveFile.h"
#include <stack>

class CWaveEditorDoc : public CDocument
{
	friend class CWaveEditorView;
	WaveFile *wave;
	CWaveEditorView *view;
	std::stack<WaveFile *> undoStack;
	std::stack<WaveFile *> redoStack;

protected:
	CWaveEditorDoc();
	DECLARE_DYNCREATE(CWaveEditorDoc)
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnToolsPlay();
	void Speedup(int start, int end);
	void Slowdown(int start, int end);
	void Echo(int start, int end);
	void Undo();
	void Redo();
	void Copy(int start, int end);
	void Cut(int start, int end);
	void Paste(int index);

	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

	virtual ~CWaveEditorDoc();

};
