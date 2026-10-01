import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessionalsAgendaComponent } from './professionals-agenda.component';

describe('ProfessionalsAgendaComponent', () => {
  let component: ProfessionalsAgendaComponent;
  let fixture: ComponentFixture<ProfessionalsAgendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessionalsAgendaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfessionalsAgendaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
