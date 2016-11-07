
// WaveEditView.cpp : implementation of the CWaveEditView class
//

#include "stdafx.h"
// SHARED_HANDLERS can be defined in an ATL project implementing preview, thumbnail
// and search filter handlers and allows sharing of document code with that project.
#ifndef SHARED_HANDLERS
#include "WaveFile.h"
#endif
#include "resource.h"
#include "WaveEditorDoc.h"
#include "WaveEditorView.h"

IMPLEMENT_DYNCREATE(CWaveEditorView, CView)

BEGIN_MESSAGE_MAP(CWaveEditorView, CScrollView)
	// Standard printing commands
	ON_COMMAND(ID_FILE_PRINT, &CScrollView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CScrollView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CScrollView::OnFilePrintPreview)
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_COMMAND(ID_EDIT_CUT, &CWaveEditorView::OnEditCut)
	ON_COMMAND(ID_EDIT_COPY, &CWaveEditorView::OnEditCopy)
	ON_COMMAND(ID_EDIT_PASTE, &CWaveEditorView::OnEditPaste)
	ON_COMMAND(ID_TOOLS_SPEEDUP, &CWaveEditorView::OnToolsSpeedup)
	ON_COMMAND(ID_TOOLS_SLOWDOWN, &CWaveEditorView::OnToolsSlowdown)
	ON_COMMAND(ID_TOOLS_ECHO, &CWaveEditorView::OnToolsEcho)
	ON_COMMAND(ID_EDIT_UNDO, &CWaveEditorView::OnEditUndo)
	ON_COMMAND(ID_EDIT_REDO, &CWaveEditorView::OnEditRedo)
END_MESSAGE_MAP()

CWaveEditorDoc* CWaveEditorView::GetDocument() const // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CWaveEditorDoc)));
	return (CWaveEditorDoc*)m_pDocument;
}

CWaveEditorView::CWaveEditorView()
{
	startSelection = 0;
	endSelection = 0;
	mousePressed = false;
}

CWaveEditorView::~CWaveEditorView()
{
}

BOOL CWaveEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	return CView::PreCreateWindow(cs);
}

void CWaveEditorView::OnDraw(CDC* pDC)
{
	CWaveEditorDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	WaveFile * wave = pDoc->wave;

	if (wave->hdr == NULL) {
		return;
	}

	CRect rect;
	GetClientRect(rect);

	COLORREF color = RGB(50, 225, 0);
	CPen pen2(PS_SOLID, 0, color);
	pDC->SelectObject(&pen2);
	CBrush brush2(color);
	pDC->SelectObject(&brush2);

	if (startSelection == endSelection) {
		pDC->MoveTo(0, 0);
		pDC->FillSolidRect(startSelection, 0, 1, rect.Height(), RGB(0, 0, 0));
	}
	else {
		pDC->MoveTo(0, 0);
		pDC->FillSolidRect(startSelection, 0, endSelection - startSelection, rect.Height(), RGB(0, 100, 255));
	}

	pDC->MoveTo(0, 0);

	unsigned int x;
	for (x = 0; x < wave->lastSample / drawScale; x++) {

		float val = wave->get_sample((int)(x*drawScale));

		int y = (int)((val + 32768) * (rect.Height() - 1) / (32767 + 32768));
		pDC->LineTo(x, rect.Height() - y);
	}

	CSize sizeTotal;
	sizeTotal.cx = x;
	SetScrollSizes(MM_TEXT, sizeTotal);
}

void CWaveEditorView::OnInitialUpdate()
{
	CScrollView::OnInitialUpdate();

	CRect rect;
	CSize sizeTotal;
	CWaveEditorDoc *doc = GetDocument();
	WaveFile * wave = doc->wave;
	doc->view = this;

	GetClientRect(rect);
	drawScale = wave->lastSample / rect.Width();
	SetScrollSizes(MM_TEXT, sizeTotal);
}

void CWaveEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	mousePressed = true;

	CPoint scrollbarPos = GetDeviceScrollPosition();
	startSelection = point.x + scrollbarPos.x;
	endSelection = point.x + scrollbarPos.x;

	CScrollView::OnLButtonDown(nFlags, point);
}

void CWaveEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	mousePressed = false;

	CPoint scrollbarPos = GetDeviceScrollPosition();
	endSelection = point.x + scrollbarPos.x;
	if (endSelection < startSelection) {
		int temp = endSelection;
		endSelection = startSelection;
		startSelection = temp;
	}
	RedrawWindow();

	CScrollView::OnLButtonUp(nFlags, point);
}

void CWaveEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	if (mousePressed) {
		CPoint scrollbarPos = GetDeviceScrollPosition();
		endSelection = point.x + scrollbarPos.x;
		RedrawWindow();
	}

	CScrollView::OnMouseMove(nFlags, point);
}

void CWaveEditorView::OnEditCut()
{
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CRect rect;
	GetClientRect(rect);

	CSize size = GetTotalSize();

	double startms = wave->lastSample * (double)this->startSelection / (double)size.cx;
	double endms = wave->lastSample * (double)this->endSelection / (double)size.cx;

	pDoc->Cut(startms, endms);

	endSelection = startSelection;

	this->RedrawWindow();
}

void CWaveEditorView::OnEditCopy()
{
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CRect rect;
	GetClientRect(rect);

	CSize size = GetTotalSize();
	double startms = wave->lastSample * (double)this->startSelection / (double)size.cx;
	double endms = wave->lastSample * (double)this->endSelection / (double)size.cx;

	pDoc->Copy(startms, endms);
}

void CWaveEditorView::OnEditPaste()
{
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CRect rect;
	GetClientRect(rect);
	CSize size = GetTotalSize();

	double startms = wave->lastSample * (double)this->startSelection / (double)size.cx;
	pDoc->Paste(startms);

	RedrawWindow();
}

void CWaveEditorView::OnToolsSpeedup()
{
	CWaveEditorDoc *doc = GetDocument();

	int start, end;
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CSize size = GetTotalSize();

	start = wave->lastSample * (double)this->startSelection / (double)size.cx;
	end = wave->lastSample * (double)this->endSelection / (double)size.cx;

	doc->Speedup(start, end);
	RedrawWindow();
}


void CWaveEditorView::OnToolsSlowdown()
{
	CWaveEditorDoc *doc = GetDocument();

	int start, end;
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CSize size = GetTotalSize();

	start = wave->lastSample * (double)this->startSelection / (double)size.cx;
	end = wave->lastSample * (double)this->endSelection / (double)size.cx;

	doc->Slowdown(start, end);
	RedrawWindow();
}


void CWaveEditorView::OnToolsEcho()
{
	CWaveEditorDoc *doc = GetDocument();

	int start, end;
	CWaveEditorDoc* pDoc = GetDocument();

	WaveFile * wave = pDoc->wave;

	CSize size = GetTotalSize();

	start = wave->lastSample * (double)this->startSelection / (double)size.cx;
	end = wave->lastSample * (double)this->endSelection / (double)size.cx;

	doc->Echo(start, end);
	RedrawWindow();
}

void CWaveEditorView::OnEditRedo()
{
	CWaveEditorDoc *doc = GetDocument();
	doc->Redo();
	RedrawWindow();
}

void CWaveEditorView::OnEditUndo()
{
	CWaveEditorDoc *doc = GetDocument();
	doc->Undo();
	RedrawWindow();
}