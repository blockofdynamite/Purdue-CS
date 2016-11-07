
// WaveEditView.h : interface of the CWaveEditView class
//

#pragma once


class CWaveEditorView : public CScrollView
{
private:
	bool mousePressed;
	int startSelection;
	int endSelection;
	double drawScale;

protected: 
	CWaveEditorView();
	DECLARE_DYNCREATE(CWaveEditorView)
	virtual void OnDraw(CDC* pDC); 
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	virtual void OnInitialUpdate();	
	DECLARE_MESSAGE_MAP()

public:
	CWaveEditorDoc* GetDocument() const;
	virtual ~CWaveEditorView();
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);
	afx_msg void OnLButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnEditCut();
	afx_msg void OnEditCopy();
	afx_msg void OnEditPaste();
	afx_msg void OnToolsSpeedup();
	afx_msg void OnToolsSlowdown();
	afx_msg void OnToolsEcho();
	afx_msg void OnEditUndo();
	afx_msg void OnEditRedo();
};

