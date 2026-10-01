import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulesAgendaComponent } from './schedules-agenda.component';

describe('SchedulesAgendaComponent', () => {
  let component: SchedulesAgendaComponent;
  let fixture: ComponentFixture<SchedulesAgendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedulesAgendaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedulesAgendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
