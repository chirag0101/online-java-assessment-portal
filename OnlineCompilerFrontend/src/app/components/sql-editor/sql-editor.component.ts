import { keymap, placeholder } from '@codemirror/view';
import { CommonModule } from '@angular/common';
import { CodeSnippetReqDTO } from '../../models/request/code-snippet-req.model';
import { AssessmentService } from '../../services/assessment.service';
import { StatusConstants } from '../../models/constants/status.constants';
import {
  ActivatedRoute,
  Router,
  RouterModule,
  RouterOutlet,
} from '@angular/router';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { MatDialog } from '@angular/material/dialog';
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
import { isPlatformBrowser } from '@angular/common';
import { EditorView, basicSetup } from 'codemirror';
import { sql } from '@codemirror/lang-sql';
import { dracula } from '@uiw/codemirror-theme-dracula';
import { eclipse } from '@uiw/codemirror-theme-eclipse';
import { debug } from 'console';
import { CodeTypes } from '../../models/constants/code-type.constants';
import { ExpireAssessmentReq } from '../../models/request/expire-assessment-req-model';
// import { oneDark } from '@codemirror/theme-one-dark';
// import { materialLight } from '@ddietr/codemirror-themes/material-light';
// import { solarizedLight } from '@ddietr/codemirror-themes/solarized-light';
// import { vscodeDark } from '@uiw/codemirror-theme-vscode';
@Component({
  selector: 'app-sql-editor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sql-editor.component.html',
  styleUrl: './sql-editor.component.css',
})
export class SqlEditorComponent implements OnInit, AfterViewInit, OnDestroy {
  private _editorRef!: ElementRef;
  assessmentVerificationIntervalId: any;

  @ViewChild('editor', { static: false }) set editorRef(ref: ElementRef) {
    if (ref) {
      this._editorRef = ref;
      if (this.isVerified && !this.editorView) {
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
    code: '',
    codeType: 2,
    url: '',
  };

  private timerDuration: number = 120 * 60 * 1000;
  private timerIntervalId: any;
  timerDisplay: string = '00:00';

  isVerified: boolean = false;
  verificationFailed: boolean = false;
  selectedTechnology: string = 'SQL';

  codeContent: string = '';
  editerOutput: string = 'OUTPUT WILL BE DISPLAYED HERE...';

  candidateId: string = '';
  candidateName: string = '';
  candidateExperience: string = '';
  candidateTechnology: string = '';
  candidateRound: string = '';

  showConfirmationDialog: boolean = false;
  dialogTitle: string = '';
  dialogMessage: string = '';

  assessmentCode: string = '';

  warned5Min: boolean = false;
  req: ExpireAssessmentReq = {
    candidateId: '',
    assessmentUrl: '',
    interviewRound: '',
  };

  private dialogResolve: ((value: boolean) => void) | null = null;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private assessmentService: AssessmentService,
    private router: Router,
    private dialog: MatDialog,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      if (sessionStorage.getItem('assessmentEnded')) {
        const currentAssessmentCode =
          this.route.snapshot.queryParamMap.get('assessmentCode');
        sessionStorage.removeItem('assessmentEnded');
        sessionStorage.removeItem('assessmentEndTime');
        window.location.reload();
      }
      this.assessmentCode =
        this.route.snapshot.queryParamMap.get('assessmentCode') ?? '';

      if (this.assessmentCode) {
        this.assessmentService
          .verifyAssessmentCode(this.assessmentCode)
          .subscribe({
            next: (apiResponse) => {
              debugger;
              if (apiResponse.statusMessage === 'SUCCESS') {
                this.isVerified = true;

                if (apiResponse.status) {
                  this.candidateId = apiResponse.response.candidateId;
                  this.candidateName = apiResponse.response.candidateFullName;
                  this.candidateExperience =
                    apiResponse.response.candidateYearsOfExpInMonths
                      .toString()
                      .concat(' Yrs');
                  this.candidateTechnology =
                    apiResponse.response.candidateTechnology;
                  this.candidateRound = apiResponse.response.interviewRound;
                }
                this.initializeCodeMirror();
              } else {
                this.verificationFailed = true;
                alert('Invalid or expired assessment link.');
                return;
              }
            },
            error: (err) => {
              console.error('Verification error:', err);
              this.verificationFailed = true;
              alert(
                'Failed to verify assessment link. Please check the link or try again.',
              );
            },
          });
      } else {
        this.verificationFailed = true;
      }
    }
  }

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
            sql(),
            eclipse,
            placeholder('WRITE YOUR QUERY HERE...'),
            EditorView.domEventHandlers({
              copy: (event) => {
                event.preventDefault();
                alert('Copying is disabled');
                return true;
              },
              cut: (event) => {
                event.preventDefault();
                return true;
              },
              paste: (event) => {
                event.preventDefault();
                alert('Pasting is disabled');
                return true;
              },
            }),
          ],
          parent: this.editorRef.nativeElement,
        });

        this.editorView.focus();

        this.startTimer();
        this.startAssessmentVerificationPolling();
        this.assessmentEndTimeCheck();
      }
    }, 0);
  }

  ngOnDestroy(): void {
    if (this.editorView) {
      this.editorView.destroy();
    }

    if (this.timerIntervalId) {
      clearInterval(this.timerIntervalId);
    }
  }

  private startAssessmentVerificationPolling(): void {
    this.assessmentVerificationIntervalId = setInterval(() => {
      if (this.assessmentVerificationIntervalId) {
        clearInterval(this.assessmentVerificationIntervalId);
      }
      if (!this.assessmentCode) {
        console.warn(
          'Assessment code missing during periodic verification. Ending session.',
        );
        this.endSessionByTimer();
        return;
      }

      this.assessmentService
        .verifyAssessmentCode(this.assessmentCode)
        .subscribe({
          next: (apiResponse) => {
            if (
              apiResponse.statusMessage !== 'SUCCESS' ||
              !apiResponse.status
            ) {
              this.endSessionByTimer();
            }
          },
          error: (err) => {
            console.error(
              'Error during periodic assessment verification:',
              err,
            );
            this.endSessionByTimer();
          },
        });
    }, 60 * 1000); // Polling interval: 60 seconds (1 minute)
  }

  saveCode(): void {
    if (!this.isVerified) {
      alert('Please verify the assessment link before compiling code.');
      return;
    }
    if (this.editorView && isPlatformBrowser(this.platformId)) {
      const currentCode = this.editorView.state.doc.toString();
      this.codeReq.candidateId = this.candidateId;
      this.codeReq.code = currentCode;
      this.codeReq.codeType = CodeTypes.SQL;
      this.codeReq.url = this.assessmentCode;

      if (this.codeReq.code.trim() !== '') {
        this.editerOutput = 'SAVING...';
      } else {
        alert("Code can't be empty!");
        this.editerOutput = 'OUTPUT WILL BE DISPLAYED HERE...';
        return;
      }

      this.assessmentService.saveCode(this.codeReq).subscribe({
        next: (response) => {
          if (response.status === StatusConstants.Success) {
            this.editerOutput = 'CODE SAVED SUCCESSFULLY!';
          } else {
            this.editerOutput = response.output;
          }
          alert('Code saved successfully!');
        },
        error: (err) => {
          console.error('Error saving code:', err);
          this.editerOutput =
            'An error occurred while communicating with the server.';
        },
      });
    }
  }

  runCode(): void {
    if (!this.isVerified) {
      alert('Please verify the assessment link before running code.');
      return;
    }
    if (this.editorView && isPlatformBrowser(this.platformId)) {
      const currentCode = this.editorView.state.doc.toString();
      this.codeReq.candidateId = this.candidateId;
      this.codeReq.code = currentCode;
      this.codeReq.url = this.assessmentCode;

      if (this.codeReq.code.trim() !== '') {
        this.editerOutput = 'RUNNING...';
      } else {
        alert("Code can't be empty!");
        return;
      }

      this.assessmentService.runCode(this.codeReq).subscribe({
        next: (response) => {
          this.editerOutput = response.output;
        },
        error: (err) => {
          console.error('Error running code:', err);
          this.editerOutput =
            'An error occurred while communicating with the compiler service.';
        },
      });
    }
  }

  resetCode(): void {
    if (!this.isVerified) {
      alert('Please verify the assessment link before resetting code.');
      return;
    }
    if (this.editorView && isPlatformBrowser(this.platformId)) {
      this.editorView.dispatch({
        changes: {
          from: 0,
          to: this.editorView.state.doc.length,
          insert: '',
        },
      });

      this.codeContent = '';
      this.editerOutput = 'OUTPUT WILL BE DISPLAYED HERE...';
      this.codeReq.code = '';
    }
  }

  endSession(): void {
    this.req.candidateId = this.candidateId;
    this.req.assessmentUrl = this.assessmentCode;
    this.req.interviewRound = this.candidateRound;
    this.openConfirmationDialogBox().then((flag: boolean) => {
      if (!flag) {
        return;
      }
      this.assessmentService.endAssessment(this.req).subscribe({
        next: () => {
          sessionStorage.removeItem('assessmentEndTime');
          sessionStorage.setItem('assessmentEnded', 'true');

          this.router.navigate(['/thankYou']);
        },
        error: (err) => {
          alert('Failed to end assessment. Please try again.');
        },
      });
    });
  }

  openConfirmationDialogBox(): Promise<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: { message: 'Do you really want to end the Assessment?' },
    });

    return dialogRef
      .afterClosed()
      .toPromise()
      .then((result) => {
        return result === true;
      });
  }

  private startTimer(): void {
    let endTime = sessionStorage.getItem('assessmentEndTime');

    if (!endTime) {
      const newEndTime = Date.now() + this.timerDuration;
      sessionStorage.setItem('assessmentEndTime', newEndTime.toString());
      endTime = newEndTime.toString();
    }

    this.runTimer(parseInt(endTime));
  }

  private runTimer(endTime: number): void {
    this.timerIntervalId = setInterval(() => {
      const now = Date.now();
      const remaining = endTime - now;
      if (
        !this.warned5Min &&
        remaining <= 5 * 60 * 1000 &&
        remaining > 4 * 60 * 1000
      ) {
        this.warned5Min = true;
        alert('Last 5 minutes!');
      }

      if (remaining <= 0) {
        this.timerDisplay = '00:00';
        clearInterval(this.timerIntervalId);
        this.endSessionByTimer();
      } else {
        this.timerDisplay = this.formatTime(remaining);
      }
    }, 1000);
  }

  assessmentEndTimeCheck(): void {
    this.assessmentService
      .getAssessmentEndTimeByCandId(this.candidateId)
      .subscribe({
        next: (res) => {
          if (res.status) {
            const receivedDate = new Date(res.response);

            const newEndTimeMillis = receivedDate.getTime();

            sessionStorage.clear();

            sessionStorage.setItem(
              'assessmentEndTime',
              newEndTimeMillis.toString(),
            );
          }
        },
      });
  }

  endSessionByTimer(): void {
    sessionStorage.removeItem('assessmentEndTime');
    sessionStorage.setItem('assessmentEnded', 'true');
    this.req.candidateId = this.candidateId;
    this.req.assessmentUrl = this.assessmentCode;
    this.req.interviewRound = this.candidateRound;

    this.assessmentService.endAssessment(this.req).subscribe({
      next: () => {
        this.router.navigate(['/thankYou']);
      },
      error: () => alert('Failed to end assessment. Please try again.'),
    });
  }

  private formatTime(ms: number): string {
    const totalSeconds = Math.floor(ms / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${this.padZero(minutes)}:${this.padZero(seconds)}`;
  }

  private padZero(num: number): string {
    return num < 10 ? `0${num}` : `${num}`;
  }

  openTextEditor(): void {
    this.router.navigate(['/textEditor'], { queryParamsHandling: 'preserve' });
  }

  openJavaEditor(): void {
    this.router.navigate(['/assessment'], { queryParamsHandling: 'preserve' });
  }

  openJsonEditor(): void {
    this.router.navigate(['/jsonEditor'], { queryParamsHandling: 'preserve' });
  }
}
