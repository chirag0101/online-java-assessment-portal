import { keymap, placeholder, EditorView } from '@codemirror/view';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { CodeSnippetReqDTO } from '../../models/code-snippet-req.model';
import { SubmissionDetails } from '../../models/submission-details.model';
import { ActivatedRoute, Router, RouterModule, RouterOutlet } from '@angular/router';
import { Component, ElementRef, ViewChild, AfterViewInit, Inject, PLATFORM_ID, OnDestroy, OnInit } from '@angular/core';
import { java } from '@codemirror/lang-java';
import { dracula } from '@uiw/codemirror-theme-dracula';
import { basicSetup } from 'codemirror';

@Component({
  selector: 'app-code-submission-info', 
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './code-submission-info.component.html', 
  styleUrls: ['./code-submission-info.component.css']
})
export class CodeSubmissionInfoComponent implements OnInit, AfterViewInit, OnDestroy {
  private _editorRef!: ElementRef;
  @ViewChild('editor', { static: false }) set editorRef(ref: ElementRef) {
    if (ref) {
      this._editorRef = ref;
      if (!this.editorView) {
        this.initializeCodeMirror();
      }
    }
  }
  get editorRef(): ElementRef {
    return this._editorRef;
  }

  editorView!: EditorView;

  codeReq: CodeSnippetReqDTO = {
    candidateId: '',
    code: ''
  };

  jdkVersion = '17.0.12'; 

  isVerified: boolean = false; 
  verificationFailed: boolean = false;
  isViewingSubmission: boolean = false; 

  codeContent: string = '';
  editerOutput: string = '';

  candidateId: string = '';
  candidateName: string = '';
  candidateAction:string='';
  candidateStatus:string='';

  submissionDetails: SubmissionDetails | undefined;

  private dialogResolve: ((value: boolean) => void) | null = null; 

  constructor(@Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private route: ActivatedRoute) { 
      
      const navigation = this.router.getCurrentNavigation();
      
      if (navigation?.extras?.state) {
        
        //getting the code object sent from view-submissions component
        this.submissionDetails = navigation.extras.state['submissionDetails'];

        if (this.submissionDetails) {
          this.isViewingSubmission = true; 
          this.codeContent = this.submissionDetails.code;
          this.editerOutput = this.submissionDetails.output === null ? 'N/A' : this.submissionDetails.output;

          this.candidateId = this.submissionDetails.candidateId;
          this.candidateName = this.submissionDetails.candidateFullName;
          this.candidateAction=this.submissionDetails.actionPerformed;
          this.candidateStatus=this.submissionDetails.status;
        }
      }
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  private initializeCodeMirror(): void {
    setTimeout(() => {
      if (isPlatformBrowser(this.platformId) && this.editorRef && this.editorRef.nativeElement) {
        if (this.editorView) {
          this.editorView.destroy(); 
        }

        this.editorView = new EditorView({
          doc: this.codeContent,
          extensions: [
            basicSetup,
            java(),
            dracula,
            placeholder(""), 
            EditorView.editable.of(false),  //setting editor to be editable as false
            keymap.of([
              { key: "Mod-c", run: () => true, preventDefault: true },
              { key: "Mod-x", run: () => true, preventDefault: true },
              { key: "Mod-v", run: () => true, preventDefault: true },
              { key: "Mod-Shift-v", run: () => true, preventDefault: true }
            ]),
            EditorView.domEventHandlers({
              copy: (event) => { event.preventDefault(); return true; },
              cut: (event) => { event.preventDefault(); return true; },
              paste: (event) => { event.preventDefault(); return true; },
              beforecopy: (event) => { event.preventDefault(); return true; },
              beforecut: (event) => { event.preventDefault(); return true; },
              beforepaste: (event) => { event.preventDefault(); return true; }
            })
          ],
          parent: this.editorRef.nativeElement
        });
      } else {
        console.warn('Could not initialize CodeMirror: editorRef or nativeElement not available, or not in browser platform.');
      }
    }, 0);
  }

  ngOnDestroy(): void {
    if (this.editorView) {
      this.editorView.destroy();
    }
  }

  goBack(): void {
    this.router.navigate(['/view-submissions']);
  }
}
