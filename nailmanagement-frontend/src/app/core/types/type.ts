export interface Servico {
  id: string;
  name: string;
  value: number;
}

export interface Agente {
  id: string;
  name: string;
  area: string;
}

export interface Agenda {
  client_id: string;
  procedure_id: string;
  agent_id: string;
  schedule: string;
}

export interface AgendaResponse {
  id: string;
  client_name: string;
  procedure_name: string;
  agent_name: string;
  schedule: string;
  value: number;
}

export interface EventoProps {
  eventoId: string;
  clienteId: string;
  servicoId: string;
  profissionalId: string;
}

export interface Cliente {
  id: string;
  username: string;
  password: string;
  email: string;
  email_validated: boolean;
  role: string;
  last_email_sent: Date;
}

export interface RetornoUser {
  sub: string; // username
  role: string; // role do usuário
  iat: number; // issued at
  exp: number; // expiration
}
