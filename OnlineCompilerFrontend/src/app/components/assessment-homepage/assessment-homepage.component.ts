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
import { java } from '@codemirror/lang-java';
import { dracula } from '@uiw/codemirror-theme-dracula';
import { debug } from 'console';
// import { oneDark } from '@codemirror/theme-one-dark';
// import { materialLight } from '@ddietr/codemirror-themes/material-light';
// import { solarizedLight } from '@ddietr/codemirror-themes/solarized-light';
// import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { eclipse } from '@uiw/codemirror-theme-eclipse';
import { ExpireAssessmentReq } from '../../models/request/expire-assessment-req-model';
import { AssessmentEndTimeReq } from '../../models/request/assessment-end-time-req.model';

@Component({
  selector: 'app-compiler-code-editor-homepage',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './assessment-homepage.component.html',
  styleUrls: ['./assessment-homepage.component.css'],
})
export class AssessmentHomepageComponent
  implements OnInit, AfterViewInit, OnDestroy
{
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
    codeType: 1,
    url: '',
  };

  jdkVersion = '';
  selectedTechnology: string = 'Java';

  private timerDuration: number = 120 * 60 * 1000;
  private timerIntervalId: any;
  timerDisplay: string = '00:00';

  isVerified: boolean = false;
  verificationFailed: boolean = false;
  verificationMessage: string = 'Verifying assessment link...';

  codeContent: string =
    'import java.util.*;\nimport java.io.*;\nimport java.lang.*;\nimport java.sql.*;\n\n//write code from here\n';
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

  assessmentEndTimeReq:AssessmentEndTimeReq={
    candidateId:'',
    round:''
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
                this.verificationMessage =
                  'Invalid or expired assessment link.';
                return;
              }
            },
            error: (err) => {
              this.isVerified = false;
              this.verificationFailed = true;
              this.verificationMessage =
                'Failed to verify assessment link. Please try again.';
            },
          });
      } else {
        this.verificationFailed = true;
        this.verificationMessage =
          'Failed to verify assessment link. Please try again.';
      }
    }
  }

  ngAfterViewInit(): void {}

  private initializeCodeMirror(): void {
    setTimeout(() => {
      if (
        isPlatformBrowser(this.platformId) &&
        this.editorRef &&
        this.editorRef.nativeElement
      ) {
        if (this.editorView) {
          this.editorView.destroy();
        }

        this.editorView = new EditorView({
          doc: this.codeContent,
          extensions: [
            basicSetup,
            java(),
            eclipse,
            placeholder('WRITE YOUR CODE HERE...'),
            keymap.of([
              { key: 'Mod-c', run: () => true, preventDefault: true },
              { key: 'Mod-x', run: () => true, preventDefault: true },
              { key: 'Mod-v', run: () => true, preventDefault: true },
              { key: 'Mod-Shift-v', run: () => true, preventDefault: true },
            ]),
            EditorView.domEventHandlers({
              copy: (event) => {
                event.preventDefault();
                return true;
              },
              cut: (event) => {
                event.preventDefault();
                return true;
              },
              paste: (event) => {
                event.preventDefault();
                return true;
              },
              beforecopy: (event) => {
                event.preventDefault();
                return true;
              },
              beforecut: (event) => {
                event.preventDefault();
                return true;
              },
              beforepaste: (event) => {
                event.preventDefault();
                return true;
              },
            }),
          ],
          parent: this.editorRef.nativeElement,
        });
        this.startTimer();
        this.getJdkVersion();
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

  getJdkVersion(): void {
    this.assessmentService.getJdkVersion().subscribe({
      next: (res) => {
        this.jdkVersion = res.response;
      },
      error: (err) => {
        console.error('Verification error:', err);
      },
    });
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

  compileCode(): void {
    if (!this.isVerified) {
      alert('Please verify the assessment link before compiling code.');
      return;
    }
    if (this.editorView && isPlatformBrowser(this.platformId)) {
      const currentCode = this.editorView.state.doc.toString();
      this.codeReq.candidateId = this.candidateId;
      this.codeReq.code = currentCode;
      this.codeReq.codeType = 1;
      this.codeReq.url = this.assessmentCode;

      if (this.codeReq.code.trim() !== '') {
        this.editerOutput = 'COMPILING...';
      } else {
        alert("Code can't be empty!");
        this.editerOutput = 'OUTPUT WILL BE DISPLAYED HERE...';
        return;
      }

      this.assessmentService.compileCode(this.codeReq).subscribe({
        next: (response) => {
          if (response.status === StatusConstants.Success) {
            this.editerOutput = 'CODE COMPILED SUCCESSFULLY!';
          } else {
            this.editerOutput = response.output;
          }
        },
        error: (err) => {
          console.error('Error compiling code:', err);
          this.editerOutput =
            'An error occurred while communicating with the compiler service.';
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
    this.assessmentEndTimeReq.candidateId=this.candidateId;
    this.assessmentEndTimeReq.round=this.candidateRound;
    this.assessmentService
      .getAssessmentEndTimeByCandId(this.assessmentEndTimeReq)
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
        error: () => alert('Failed to end assessment. Please try again.')
      });
  }

  endSessionByTimer(): void {
    sessionStorage.removeItem('assessmentEndTime');
    sessionStorage.setItem('assessmentEnded', 'true');

    this.req.candidateId = this.candidateId;
    this.req.interviewRound = this.candidateRound;
    this.req.assessmentUrl = this.assessmentCode;

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

  openJsonEditor(): void {
    this.router.navigate(['/jsonEditor'], { queryParamsHandling: 'preserve' });
  }

  openSqlEditor(): void {
    this.router.navigate(['/sqlEditor'], { queryParamsHandling: 'preserve' });
  }
}
