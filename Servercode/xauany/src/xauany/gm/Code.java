package xauany.gm;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.omg.PortableInterceptor.InvalidSlot;
import xauany.code.CodeUtils;
import xauany.code.FCode;
import gm.GmCmdResult;
import gm.annotation.Cmd;
import gm.annotation.Module;
import gm.annotation.Param;
import xdb.Trace;

@Module (comment="����ִ��GM����")
public class Code {
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private String toDateString(long time){
		if(time == 0){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		
		return df.format(c.getTime());
	}

    private long toDateMills(String datestring) throws ParseException{
        long timeMills = 0;
        if(!"0".equals(datestring)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(datestring);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 1);

            timeMills = c.getTimeInMillis();
        }

        return timeMills;
    }

    private Object toCodeSetString(int codetype) {
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("codetype", codetype);
        xbean.ActivationCodeSet codeset = xtable.Activationcodesets.get(codetype);
        if(codeset == null){
            map.put("errmsg", "�����ͻ�û�����ɼ�����");
        }
        else{
            map.put("count", codeset.getValues().size());
            map.put("platform", codeset.getPlatformset());
            map.put("createtime", toDateString(codeset.getCreatetime()));
            map.put("opentime", toDateString(codeset.getOpentime()));
            map.put("expirate", toDateString(codeset.getExpiratetime()));
            map.put("islogin", codeset.getIslogin());
        }

        return map;
    }
	
	@Cmd(comment="��ʾ��ǰ�Ѿ��еļ���������")
	public Object showall(){
		List<Object> lst = new ArrayList<>();
		xbean.GlobalActivationCode globalActivationCode = FCode.getGlobalActivationCode();
		globalActivationCode.getAlltypes().forEach(codetype -> {
		    lst.add(toCodeSetString(codetype));
		});
		
		return lst;
	}
	
	@Cmd(comment="��ʾ����������")
	public Object show(@Param(name="codetype", comment="����������") int codetype){
		return toCodeSetString(codetype);
	}

	@Cmd(comment = "�޸������")
    public Object modify(@Param(name="codetype", comment = "������")int codetype,
                        @Param(name="activatelogin",comment = "�Ƿ�Ϊ��½������ 0|1") int activatelogin,
                         @Param(name="infinite", comment = "�Ƿ���ʹ�ô��� 0|1") int isShare,
                         @Param(name="opentime", comment = "����ʱ��,��ʽyyyy-MM-dd���賿����, ��0��ʾ����") String opentime,
                         @Param(name="expiretime", comment = "����ʱ�䣬��ʽyyyy-MM-dd���賿����, ��0��ʾ��������") String expiretime,
                         @Param(name="platforms", comment = "����ƽ̨,������,��:����,���� 342:123,0��-1��ʾ������") String plats

    ) {
	    try {
            xbean.ActivationCodeSet newcodeset = xtable.Activationcodesets.get(codetype);
            if (newcodeset != null) {
                final boolean isLogin = activatelogin == 1;
                final long openTime = toDateMills(opentime);
                final long expireTime = toDateMills(expiretime);
                final List<Integer> platforms = Arrays.asList(plats.split(":")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
                if (platforms.size() == 1 && (platforms.contains(-1) || platforms.contains(0)))
                    platforms.clear();

                newcodeset.setCreatetime(System.currentTimeMillis());
                newcodeset.setOpentime(openTime);
                newcodeset.setExpiratetime(expireTime);
                newcodeset.setIsshared(isShare);
                newcodeset.setIslogin(isLogin);
                newcodeset.getPlatformset().addAll(platforms);
            } else {
                return GmCmdResult.error("�����Ͳ�����");
            }
        } catch (Exception e) {
            return GmCmdResult.exception(e);
        }
	    return "succ";
    }


    @Cmd(comment="�ֶ����һ��������")
    public Object addone(
            @Param(name="codetype", comment="����������") int codetype,
            @Param(name="code", comment="������") String code
    ){
        xbean.ActivationCodeSet codeset = xtable.Activationcodesets.get(codetype);
        long codevalue = CodeUtils.decode(code);
        xbean.ActivationCode activationcode = xtable.Activationcodes.get(codevalue);
        if(activationcode != null){
            return GmCmdResult.error("�������ѱ�ռ��");
        }
        codeset.getValues().add(codevalue);
        activationcode = xbean.Pod.newActivationCode();
        activationcode.setType(codetype);
        xtable.Activationcodes.insert(codevalue, activationcode);

        return " ��ӳɹ� �� " + code;
    }

    @Cmd(comment = "���ļ�����һ��������,�������Ѵ��ڵ�������,��������������һ��,���Ҳ��ܵ����Ѵ��ڵ���,���ļ���ʽ:\n" +
        "codetype(����������, >0)\n"+
        "islogin(�Ƿ�Ϊ��½������,0|1)\n"+
        "infinite(�Ƿ�Ϊͨ��,������ʹ�ô���,0|1)\n" +
        "opentime(����ʱ�䣬��ʽyyyy-MM-dd���賿����, ��0��ʾ����)\n" +
        "expiretime(����ʱ�䣬��ʽyyyy-MM-dd���賿����, ��0��ʾ��������)\n" +
        "platforms(����ƽ̨,������,��:����,���� 342:123,�ձ�ʾ������)\n" +
        "code1\n" + "...\n" + "codeN\n"
    )
    public GmCmdResult importFromFile(@Param(name="codefile", comment = "�������ļ�")String codefile) throws Exception {
//        try {
            Trace.info("importFromFile:{} start", codefile);
            final List<String> lines = Files.readAllLines(new File(codefile).toPath(), StandardCharsets.UTF_8).stream()
                    .map(s -> s.trim()).filter(s -> !s.isEmpty() && !s.startsWith("#")).collect(Collectors.toList());
            if(lines.size() < 6)
                return GmCmdResult.error("�������ļ���ʽ���Ϸ�!");
            int index = 0;
            final int codeType = Integer.parseInt(lines.get(index++));
            final boolean isLogin = lines.get(index++).equals("1");
            final int isShare = Integer.parseInt(lines.get(index++));
            final long openTime = toDateMills(lines.get(index++));
            final long expireTime = toDateMills(lines.get(index++));
            final List<Integer> platforms = Arrays.asList(lines.get(index++).split(":")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
            if(platforms.size() == 1 && (platforms.contains(-1) || platforms.contains(0)))
                platforms.clear();

            xbean.GlobalActivationCode globalActivationCode = FCode.getGlobalActivationCode();
            List<String> newCodeSet = lines.subList(index, lines.size());

            xbean.ActivationCodeSet newcodeset = xtable.Activationcodesets.get(codeType);
            if(newcodeset == null) {
                newcodeset = xbean.Pod.newActivationCodeSet();
                globalActivationCode.getAlltypes().add(codeType);
                xtable.Activationcodesets.insert(codeType, newcodeset);

                newcodeset.setType(codeType);
                newcodeset.setCreatetime(System.currentTimeMillis());
                newcodeset.setOpentime(openTime);
                newcodeset.setExpiratetime(expireTime);
                newcodeset.setIsshared(isShare);
                newcodeset.setIslogin(isLogin);
                newcodeset.getPlatformset().addAll(platforms);
            } else {
                if(newcodeset.getOpentime() != openTime)
                    return GmCmdResult.error("�Ѵ��ڴ����ͼ�����.������ʱ�� cur:" + newcodeset.getOpentime() + " != new:" + openTime);
                if(newcodeset.getIslogin() != isLogin) {
                    return GmCmdResult.error("�Ѵ��ڴ����ͼ�����.����½���� cur:" + newcodeset.getIslogin() + " != new:" + isLogin);
                }
                if(newcodeset.getExpiratetime() != expireTime)
                    return GmCmdResult.error("�Ѵ��ڴ����ͼ�����.������ʱ�� cur:" + newcodeset.getExpiratetime() + " != new:" + expireTime);
                if(newcodeset.getIsshared() != isShare)
                    return GmCmdResult.error("�Ѵ��ڴ����ͼ�����.��ͨ������ cur:" + newcodeset.getIsshared() + " != new:" + isShare);

                final Set<Integer> curPlatforms = newcodeset.getPlatformset();
                if(curPlatforms.size() != platforms.size() || !curPlatforms.containsAll(platforms))
                    return GmCmdResult.error("�Ѵ��ڴ����ͼ�����.��ƽ̨���� cur:" + curPlatforms + " != new:" + platforms);
            }

            final Set<Long> codes = newcodeset.getValues();
            for(String codeStr : newCodeSet) {
                final long code = CodeUtils.decode(codeStr);
                if(xtable.Activationcodes.get(code) != null)
                    return GmCmdResult.error("code:" + codeStr + " �Ѿ�����!");
                codes.add(code);
                xbean.ActivationCode activationcode = xbean.Pod.newActivationCode();
                activationcode.setType(codeType);
                xtable.Activationcodes.insert(code, activationcode);
            }
            Trace.info("importFromFile:{} end", codefile);
            return GmCmdResult.success("import succ");
//        } catch (IOException e) {
//            Trace.error("open codefile:{} fail", codefile);
//            return GmCmdResult.exception(e);
//        } catch (Exception e) {
//            return GmCmdResult.exception(e);
//        }
    }

	
	@Cmd(comment="����Ŀ¼��������,�ļ����Ʊ�����*.txt")
	public Object importFromDirectory(@Param(name="codeFolder", comment="�����������ļ���") String codeFolder) throws Exception {
		File folder = new File(codeFolder);
		Map<String, Object> result = new LinkedHashMap<>();
		for (File file : folder.listFiles()) {
			String filename = file.getPath();
            if(file.isDirectory()) {
                result.put(filename, importFromDirectory(filename));
            } else if (filename.endsWith(".txt")) {
                result.put(filename,  importFromFile(filename));
			}
		}
		
		return result;
	}

	@Cmd(comment="ɾ��ĳһ���͵����м�����")
	public Object removeCodeSet(
			@Param(name="codetype", comment="����������") int codetype) throws ParseException{
		xbean.GlobalActivationCode globalActivationCode = FCode.getGlobalActivationCode();
		if(globalActivationCode == null || !globalActivationCode.getAlltypes().contains(codetype)){
			return GmCmdResult.error("û�е�ǰ�ļ���������");
		}
		if(globalActivationCode != null){
			globalActivationCode.getAlltypes().remove(codetype);
		}
		xbean.ActivationCodeSet codeset = xtable.Activationcodesets.get(codetype);
		xtable.Activationcodesets.remove(codetype);
		codeset.getValues().forEach(code -> {
			xtable.Activationcodes.remove(code);
		});
		globalActivationCode.getAlltypes().remove(codetype);
		
		return codeset;
	}

	@Cmd(comment = "������м�����")
    public Object removeAll() {
	    xtable.Globalactivationcodes.delete(0);
        final List<Integer> types = new ArrayList<>();
        xtable.Activationcodesets.getTable().walk((type, s) -> {
            types.add(type);
            return true;
        });
        for(int type : types) {
            xtable.Activationcodesets.remove(type);
        }
        final List<Long> codes = new ArrayList<>(5000000);
        xtable.Activationcodes.getTable().walk((c, d) -> {
            codes.add(c);
            return true;
        });
        for(long code : codes) {
            xtable.Activationcodes.remove(code);
        }
	    return String.format("types:%s codes:%s", types.size(), codes.size());
    }
}