import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-thank-you',
  imports: [],
  templateUrl: './thank-you.component.html',
  styleUrl: './thank-you.component.css'
})
export class ThankYouComponent implements OnInit{
  ngOnInit(): void {
    sessionStorage.clear();
  }
  
}
