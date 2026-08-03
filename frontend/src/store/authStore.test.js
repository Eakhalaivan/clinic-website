import { describe, it, expect } from 'vitest';
import { parseJwtPayload } from './authStore';

describe('parseJwtPayload', () => {
  it('correctly decodes standard base64url JWT payload containing - and _', () => {
    // Payload JSON: {"sub":"user@example.com","roles":["ROLE_DOCTOR"],"userId":123,"test":"url_safe-payload?"}
    // Base64url encoded payload: eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9ET0NUT1IiXSwidXNlcklkIjoxMjMsInRlc3QiOiJ1cmxfc2FmZS1wYXlsb2FkPyJ9
    const fakeToken = `header.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9ET0NUT1IiXSwidXNlcklkIjoxMjMsInRlc3QiOiJ1cmxfc2FmZS1wYXlsb2FkPyJ9.signature`;

    const parsed = parseJwtPayload(fakeToken);

    expect(parsed.sub).toBe('user@example.com');
    expect(parsed.roles).toEqual(['ROLE_DOCTOR']);
    expect(parsed.userId).toBe(123);
    expect(parsed.test).toBe('url_safe-payload?');
  });

  it('throws an error for malformed token structure', () => {
    expect(() => parseJwtPayload('invalidtoken')).toThrow('Malformed JWT: missing payload segment');
  });
});
