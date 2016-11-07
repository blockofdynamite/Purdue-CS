
// WaveEditDoc.cpp : implementation of the CWaveEditDoc class
//

#include "stdafx.h"
#include "WaveEditor.h"
#include "FilterManager.h"
#ifndef SHARED_HANDLERS
#include "WaveFile.h"
#endif

#include "WaveEditorDoc.h"

#include <propkey.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

// CWaveEditDoc

IMPLEMENT_DYNCREATE(CWaveEditorDoc, CDocument)

BEGIN_MESSAGE_MAP(CWaveEditorDoc, CDocument)
	ON_COMMAND(ID_TOOLS_PLAY, &CWaveEditorDoc::OnToolsPlay)
END_MESSAGE_MAP()


// CWaveEditDoc construction/destruction

CWaveEditorDoc::CWaveEditorDoc()
{
	// TODO: add one-time construction code here
	wave = new WaveFile();
}

CWaveEditorDoc::~CWaveEditorDoc()
{

}

BOOL CWaveEditorDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}


// CWaveEditDoc serialization

void CWaveEditorDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
		wave->read(ar.GetFile());
	}
}

// CWaveEditDoc commands


void CWaveEditorDoc::OnToolsPlay()
{
	wave->play();
}

void CWaveEditorDoc::Speedup(int start, int end)
{
	WaveFile *newWave = FilterManager::getSpeedUp(wave, start, end);

	undoStack.push(wave);

	while (!redoStack.empty()) {
		redoStack.pop();
	}
	
	wave = newWave;
}

void CWaveEditorDoc::Slowdown(int start, int end)
{
	WaveFile *newWave = FilterManager::getSlowDown(wave, start, end);

	undoStack.push(wave);

	while (!redoStack.empty()) {
		redoStack.pop();
	}

	wave = newWave;
}

void CWaveEditorDoc::Echo(int start, int end)
{
	WaveFile *newWave = FilterManager::getEcho(wave, start, end);

	undoStack.push(wave);
	
	while (!redoStack.empty()) {
		redoStack.pop();
	}

	wave = newWave;
}

void CWaveEditorDoc::Undo()
{
	if (undoStack.empty()) {
		return;
	}

	redoStack.push(wave);
	wave = undoStack.top();
	undoStack.pop();
}

void CWaveEditorDoc::Redo()
{
	if (redoStack.empty()) {
		return;
	}

	undoStack.push(wave);
	wave = redoStack.top();
	redoStack.pop();
}

void CWaveEditorDoc::Copy(int start, int end)
{
	theApp.clipboard = wave->get_fragment(start, end);
}

void CWaveEditorDoc::Cut(int start, int end)
{
	Copy(start, end);

	WaveFile * w2 = wave->remove_fragment(start, end);

	undoStack.push(wave);
	
	while (!redoStack.empty()) {
		redoStack.pop();
	}

	wave = w2;
}

void CWaveEditorDoc::Paste(int index)
{
	WaveFile * wave2 = wave->insert_fragment(theApp.clipboard, index);

	undoStack.push(wave);

	while (!redoStack.empty()) {
		redoStack.pop();
	}

	wave = wave2;
}

