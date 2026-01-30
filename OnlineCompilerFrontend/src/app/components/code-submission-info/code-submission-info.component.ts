import { keymap, placeholder, EditorView } from '@codemirror/view';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { CodeSnippetReqDTO } from '../../models/request/code-snippet-req.model';
import { SubmissionDetails } from '../../models/submission-details.model';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import {
  Component,
  ElementRef,
  ViewChild,
  AfterViewInit,
  Inject,
  PLATFORM_ID,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { java } from '@codemirror/lang-java';
import { dracula } from '@uiw/codemirror-theme-dracula';
import { basicSetup } from 'codemirror';
import { eclipse } from '@uiw/codemirror-theme-eclipse';

@Component({
  selector: 'app-code-submission-info',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './code-submission-info.component.html',
  styleUrls: ['./code-submission-info.component.css'],
})
export class CodeSubmissionInfoComponent
  implements OnInit, AfterViewInit, OnDestroy
{
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
  jdkVersion = '17.0.12';
  receivedTech: string = '';

  codeContent: string = '';
  editerOutput: string = '';
  candidateId: string = '';
  candidateName: string = '';
  candidateAction: string = '';
  candidateStatus: string = '';
  candidateRound: string = '';

  stateCandidateId: string = '';
  stateInterviewerId: string = '';
  stateRound: string = '';
  stateAssessmentEndTime = '';
  stateLangType = '';

  submissionDetails: SubmissionDetails | undefined;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    const navigation = this.router.getCurrentNavigation();

    if (navigation?.extras?.state) {
      this.submissionDetails = navigation.extras.state['submissionDetails'];
      this.receivedTech = navigation.extras.state['tech'];
      this.stateAssessmentEndTime =
        navigation.extras.state['assessmentEndTime'];
      this.stateRound = navigation.extras.state['round'];
      this.stateLangType = navigation.extras.state['langType'];
      this.stateInterviewerId = navigation.extras.state['interviewerId'];
      this.stateCandidateId = navigation.extras.state['candidateId'];

      if (this.submissionDetails) {
        debugger;
        this.codeContent = this.submissionDetails.code;
        this.editerOutput =
          this.submissionDetails.output === null
            ? 'N/A'
            : this.submissionDetails.output;
        this.candidateId = this.submissionDetails.candidateId;
        this.candidateName = this.submissionDetails.candidateFullName;
        this.candidateAction = this.submissionDetails.actionPerformed;
        this.candidateStatus =
          this.submissionDetails.status === null
            ? 'N/A'
            : this.submissionDetails.status;
        this.candidateRound =
          this.submissionDetails.round === null
            ? 'N/A'
            : this.submissionDetails.round;
      }
    }
  }

  ngOnInit(): void {}
  ngAfterViewInit(): void {}

  private initializeCodeMirror(): void {
    setTimeout(() => {
      if (isPlatformBrowser(this.platformId) && this.editorRef?.nativeElement) {
        if (this.editorView) {
          this.editorView.destroy();
        }

        this.editorView = new EditorView({
          doc: this.codeContent,
          extensions: [
            basicSetup,
            java(),
            eclipse,
            placeholder(''),
            EditorView.editable.of(false),
            keymap.of([
              { key: 'Mod-c', run: () => true, preventDefault: true },
              { key: 'Mod-x', run: () => true, preventDefault: true },
              { key: 'Mod-v', run: () => true, preventDefault: true },
            ]),
          ],
          parent: this.editorRef.nativeElement,
        });
      }
    }, 0);
  }

  ngOnDestroy(): void {
    if (this.editorView) {
      this.editorView.destroy();
    }
  }

  fetchDetailedInfo(langType: string): void {
    this.router.navigate(['/viewSubmissions'], {
      state: {
        tech: langType,
        candidate: this.stateCandidateId,
        round: this.stateRound,
        interviewer: this.stateInterviewerId,
        assessmentEndTime: this.stateAssessmentEndTime
      },
    });
  }
}
