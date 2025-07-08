--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.5 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: amministratorefile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.amministratorefile (
    nome character varying(20) NOT NULL,
    file text NOT NULL,
    difficolta character varying(20) NOT NULL,
    lingua character varying(10) NOT NULL
);


ALTER TABLE public.amministratorefile OWNER TO postgres;

--
-- Name: amministratorestopwords; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.amministratorestopwords (
    nome character varying(20) NOT NULL,
    stopword character varying(20) NOT NULL,
    lingua character varying(10) DEFAULT 'IT'::character varying
);


ALTER TABLE public.amministratorestopwords OWNER TO postgres;

--
-- Name: punteggi; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.punteggi (
    nomeutente character varying(20) NOT NULL,
    punteggio integer NOT NULL,
    dataora timestamp without time zone NOT NULL,
    difficolta character varying(20) NOT NULL,
    lingua character varying(10) DEFAULT 'IT'::character varying
);


ALTER TABLE public.punteggi OWNER TO postgres;

--
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    nome character varying(20) NOT NULL,
    password character varying(20) NOT NULL,
    email character varying(30) NOT NULL,
    ruolo character varying(20) DEFAULT 'Utente'::character varying NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- Data for Name: amministratorefile; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.amministratorefile (nome, file, difficolta, lingua) FROM stdin;
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/viaggiare.txt	Medio	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/creatività.txt	Medio	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/ironman.txt	Facile	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/universo.txt	Facile	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/moschettieri.txt	Facile	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/cristiano.txt	Facile	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/Manzoni.txt	Medio	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/Annibale.txt	Medio	IT
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/environment_text_2.txt	Facile	EN
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/history_text_3.txt	Facile	EN
AntonioAdmin	/Users/antoniobellofatto/NetBeansProjects/Wordageddon/testi/technology_text_1.txt	Facile	EN
\.


--
-- Data for Name: amministratorestopwords; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.amministratorestopwords (nome, stopword, lingua) FROM stdin;
AntonioAdmin	a	IT
AntonioAdmin	ad	IT
AntonioAdmin	al	IT
AntonioAdmin	alle	IT
AntonioAdmin	allo	IT
AntonioAdmin	anche	IT
AntonioAdmin	ancora	IT
AntonioAdmin	avere	IT
AntonioAdmin	b	IT
AntonioAdmin	c	IT
AntonioAdmin	come	IT
AntonioAdmin	con	IT
AntonioAdmin	coi	IT
AntonioAdmin	col	IT
AntonioAdmin	d	IT
AntonioAdmin	da	IT
AntonioAdmin	del	IT
AntonioAdmin	dello	IT
AntonioAdmin	dei	IT
AntonioAdmin	degli	IT
AntonioAdmin	delle	IT
AntonioAdmin	e	IT
AntonioAdmin	ed	IT
AntonioAdmin	essere	IT
AntonioAdmin	f	IT
AntonioAdmin	fare	IT
AntonioAdmin	fui	IT
AntonioAdmin	fummo	IT
AntonioAdmin	furono	IT
AntonioAdmin	fu	IT
AntonioAdmin	foste	IT
AntonioAdmin	fosse	IT
AntonioAdmin	fossero	IT
AntonioAdmin	g	IT
AntonioAdmin	h	IT
AntonioAdmin	i	IT
AntonioAdmin	io	IT
AntonioAdmin	in	IT
AntonioAdmin	invece	IT
AntonioAdmin	j	IT
AntonioAdmin	k	IT
AntonioAdmin	l	IT
AntonioAdmin	la	IT
AntonioAdmin	là	IT
AntonioAdmin	li	IT
AntonioAdmin	lo	IT
AntonioAdmin	loro	IT
AntonioAdmin	m	IT
AntonioAdmin	ma	IT
AntonioAdmin	male	IT
AntonioAdmin	me	IT
AntonioAdmin	miei	IT
AntonioAdmin	mie	IT
AntonioAdmin	mi	IT
AntonioAdmin	mio	IT
AntonioAdmin	n	IT
AntonioAdmin	ne	IT
AntonioAdmin	né	IT
AntonioAdmin	no	IT
AntonioAdmin	non	IT
AntonioAdmin	o	IT
AntonioAdmin	od	IT
AntonioAdmin	ok	IT
AntonioAdmin	ora	IT
AntonioAdmin	p	IT
AntonioAdmin	perché	IT
AntonioAdmin	poi	IT
AntonioAdmin	pure	IT
AntonioAdmin	q	IT
AntonioAdmin	quando	IT
AntonioAdmin	quanto	IT
AntonioAdmin	r	IT
AntonioAdmin	s	IT
AntonioAdmin	se	IT
AntonioAdmin	sei	IT
AntonioAdmin	sempre	IT
AntonioAdmin	siete	IT
AntonioAdmin	si	IT
AntonioAdmin	solo	IT
AntonioAdmin	soltanto	IT
AntonioAdmin	sono	IT
AntonioAdmin	suo	IT
AntonioAdmin	sua	IT
AntonioAdmin	suoi	IT
AntonioAdmin	sue	IT
AntonioAdmin	t	IT
AntonioAdmin	te	IT
AntonioAdmin	ti	IT
AntonioAdmin	tra	IT
AntonioAdmin	u	IT
AntonioAdmin	un	IT
AntonioAdmin	uno	IT
AntonioAdmin	una	IT
AntonioAdmin	un'	IT
AntonioAdmin	usare	IT
AntonioAdmin	v	IT
AntonioAdmin	va	IT
AntonioAdmin	voi	IT
AntonioAdmin	vostro	IT
AntonioAdmin	vostra	IT
AntonioAdmin	vostri	IT
AntonioAdmin	vostre	IT
AntonioAdmin	w	IT
AntonioAdmin	x	IT
AntonioAdmin	y	IT
AntonioAdmin	z	IT
AntonioAdmin	già	IT
AntonioAdmin	per	IT
AntonioAdmin	però	IT
AntonioAdmin	qui	IT
AntonioAdmin	quella	IT
AntonioAdmin	quelle	IT
AntonioAdmin	quello	IT
AntonioAdmin	quelli	IT
AntonioAdmin	il	IT
AntonioAdmin	nel	IT
AntonioAdmin	della	IT
AntonioAdmin	di	IT
AntonioAdmin	che	IT
AntonioAdmin	le	IT
AntonioAdmin	vdiugsiu	IT
AntonioAdmin	ronaldo	IT
AntonioAdmin	league	EN
\.


--
-- Data for Name: punteggi; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.punteggi (nomeutente, punteggio, dataora, difficolta, lingua) FROM stdin;
Antonio	5	2025-06-25 17:44:38.821	Medio	IT
Antonio	5	2025-06-25 17:51:16.463	Facile	IT
Giorgio	5	2025-06-25 17:52:12.62	Medio	IT
Antonio	5	2025-06-25 17:52:49.472	Medio	IT
Antonio	30	2025-06-25 18:18:33.139	Medio	IT
Antonio	0	2025-06-25 20:47:41.535	Medio	IT
Antonio	90	2025-06-25 20:49:43.346	Difficile	IT
Antonio	30	2025-06-27 12:32:34.437	Facile	IT
Antonio	70	2025-06-27 12:35:39.603	Medio	IT
Antonio	10	2025-06-27 12:39:47.553	Facile	IT
Antonio	30	2025-06-27 12:42:53.85	Facile	IT
Antonio	50	2025-06-27 12:44:08.293	Facile	IT
Antonio	30	2025-06-27 12:51:29.348	Facile	IT
Antonio	10	2025-06-27 13:02:15.967	Facile	IT
Antonio	20	2025-06-27 13:02:28.186	Medio	IT
Antonio	30	2025-06-27 13:31:15.092	Facile	IT
AntonioAdmin	20	2025-06-27 13:31:38.84	Facile	IT
AntonioAdmin	10	2025-06-27 13:31:52.373	Medio	IT
AntonioAdmin	10	2025-06-27 13:32:20.99	Facile	IT
AntonioAdmin	30	2025-06-27 13:32:30.354	Facile	IT
AntonioAdmin	10	2025-06-27 13:32:40.286	Facile	IT
AntonioAdmin	10	2025-06-27 14:04:14.192	Facile	IT
Antonio	10	2025-06-27 18:17:24.233	Facile	IT
Antonio	80	2025-06-27 18:53:36.673	Facile	IT
Antonio	0	2025-06-27 19:01:42.034	Facile	IT
Antonio	10	2025-06-27 19:12:38.705	Facile	IT
Antonio	0	2025-06-27 19:22:54.659	Facile	IT
AntonioAdmin	40	2025-06-27 19:23:19.557	Facile	IT
Antonio	30	2025-06-27 19:27:36.935	Facile	IT
AntonioAdmin	50	2025-06-27 19:28:04.404	Facile	IT
Antonio	40	2025-06-27 19:29:50.417	Facile	IT
Antonio	40	2025-06-27 19:30:34.5	Facile	IT
Antonio	50	2025-06-27 19:34:25.387	Medio	IT
Antonio	70	2025-06-27 19:39:25.649	Medio	IT
Antonio	30	2025-06-27 19:40:16.281	Facile	IT
AntonioAdmin	40	2025-06-29 16:28:02.315	Facile	IT
Antonio	10	2025-06-29 18:16:10.376	Facile	EN
\.


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utente (nome, password, email, ruolo) FROM stdin;
Antonio	2003	b.a3@gmail.com	Utente
Roberto	rob2003	rob32@gmail.com	Utente
AntonioAdmin	admin	admin@gmail.com	admin
Giorgio	giorgio	dsgd@gmail.com	Utente
Mario	2000m	mario@gmail.com	Utente
\.


--
-- Name: amministratorefile amministratorefile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratorefile
    ADD CONSTRAINT amministratorefile_pkey PRIMARY KEY (nome, file);


--
-- Name: amministratorestopwords amministratorestopwords_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratorestopwords
    ADD CONSTRAINT amministratorestopwords_pkey PRIMARY KEY (nome, stopword);


--
-- Name: punteggi punteggi_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.punteggi
    ADD CONSTRAINT punteggi_pkey PRIMARY KEY (nomeutente, dataora);


--
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (nome);


--
-- Name: amministratorefile amministratorefile_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratorefile
    ADD CONSTRAINT amministratorefile_nome_fkey FOREIGN KEY (nome) REFERENCES public.utente(nome) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: amministratorestopwords amministratorestopwords_nome_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amministratorestopwords
    ADD CONSTRAINT amministratorestopwords_nome_fkey FOREIGN KEY (nome) REFERENCES public.utente(nome) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

